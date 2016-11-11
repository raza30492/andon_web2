package in.andonsystem;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import in.andonsystem.models.Preferences;
import in.andonsystem.scheduler.FutureTaskManager;
import in.andonsystem.services.DBTableService;
import in.andonsystem.services.DeptService;
import in.andonsystem.services.DesignationService;
import in.andonsystem.services.IssueService;
import in.andonsystem.services.MiscService;
import in.andonsystem.services.ProblemService;
import in.andonsystem.services.SectionService;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class ContextListener implements ServletContextListener{
    
    //private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> fixIssueScheduler;
    FutureTaskManager taskManager;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        taskManager = new FutureTaskManager();
        taskManager.start();
        
        ServletContext context = sce.getServletContext();
        Connection conn = DBase.getConn();
        DBTableService dbService = new DBTableService(conn);
            try{
            //Create Tables 
            dbService.createUserTable();
            dbService.createDeptTable();
            dbService.createSectionTable();
            dbService.createProblemTable();
            dbService.createDesignationTable();
            dbService.createDesignationLineTable();
            dbService.createDesignationProblemTable();
            dbService.createIssueTable();
            dbService.createForgotPasswordTable();
            dbService.createForgotPasswordEvent();
            //dbService.addSections();
            //dbService.addDepartments();

            //Instantiate Services
            MiscService mService = new MiscService();
            ProblemService pService = new ProblemService(conn);
            DeptService dService = new DeptService(conn);
            SectionService sService = new SectionService(conn);
            DesignationService desgnService = new DesignationService(conn);

            //Write the APP_LAUNCH time in properties file
            long timeNow = System.currentTimeMillis();        //GMT time online
            mService.writeProperties(context,Preferences.APP_LAUNCH, "" + timeNow);

            //Schedule automatic fixing of issue if left unfixed till factory close
            long days = TimeUnit.MILLISECONDS.toDays(timeNow);
            long millisToday = (timeNow - TimeUnit.DAYS.toMillis(days));  //GMT:This number of milliseconds after 12:00 today
            int minutesToday = (int )TimeUnit.MILLISECONDS.toMinutes(millisToday);  //GMT       
            minutesToday += (60*5) + 30;  //GMT+05:30 minutesToday 

            int endHour = Integer.parseInt(mService.readProperties(context, Preferences.END_HOUR));
            int endMinute = Integer.parseInt(mService.readProperties(context, Preferences.END_MINUTE));
            int factoryCloseMinute = (60*endHour) + endMinute;

            int initialDelay = 0;
            if(minutesToday <= factoryCloseMinute){
                initialDelay = (factoryCloseMinute - minutesToday) + 1;
            }else{
                initialDelay = ((24*60) - minutesToday) + factoryCloseMinute + 1;
            }
            System.out.println("Initial Schedule delay(in minutes): "+initialDelay);
            
            ScheduledExecutorService scheduler = FutureTaskManager.getScheduler();
            
            FixIssuesThread thread = new FixIssuesThread(endHour, endMinute);
            fixIssueScheduler = scheduler.scheduleAtFixedRate(thread, initialDelay, (60*24), TimeUnit.MINUTES);


            //Initialize Application Scope Attributes
            //Set number of Lines
            context.setAttribute(
                    "lines",
                    mService.readProperties(context, Preferences.LINES)
            );
            //Set all designation names
            context.setAttribute("designations",desgnService.getDesgns());
            //Set Problems
            context.setAttribute("problems", pService.getProbs());
            //Set Departments
            context.setAttribute("depts", dService.getDepartments());
            //Set Sections
            context.setAttribute("sections", sService.getSections());
            context.setAttribute("start_hour", mService.readProperties(context, Preferences.START_HOUR));
            context.setAttribute("start_minute", mService.readProperties(context, Preferences.START_MINUTE));
            context.setAttribute("end_hour", mService.readProperties(context, Preferences.END_HOUR));
            context.setAttribute("end_minute", mService.readProperties(context, Preferences.END_MINUTE));

            context.setAttribute("time_ack", mService.readProperties(context, Preferences.TIME_ACK));
            context.setAttribute("time_level1", mService.readProperties(context, Preferences.TIME_LEVEL1));
            context.setAttribute("time_level2", mService.readProperties(context, Preferences.TIME_LEVEL2));


        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                conn.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ContextDestroyed()");
        
        Boolean result = fixIssueScheduler.cancel(true);
        if(result){
            System.out.println("Automatic Fix Thread Stopped Successfully");
        }      
        FutureTaskManager.getScheduler().shutdownNow();
        taskManager.shutdown();
        try{
            AbandonedConnectionCleanupThread.shutdown();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * This is Thread used to Schedule Automatic Fix of Issues
     */
    
    class FixIssuesThread implements Runnable{
        private int endHour;
        private int endMinute;

        public FixIssuesThread(int endHour,int endMinute){
            this.endHour = endHour;
            this.endMinute = endMinute;
        }
        
        @Override
        public void run() {   
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
            df.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
            long timeNow = System.currentTimeMillis();
            String date = df.format(new Date(timeNow));
            
            System.out.println(new Date(timeNow) + ", FixIssuesThread.run()");

            String time = String.format("%02d:%02d:00", endHour,endMinute);
            String datetime = date + time;
            System.out.println("all unfixed issues being fixed with fixAt time = "+datetime);
            
            Connection conn = DBase.getConn();
            IssueService iService = new IssueService(conn);
            try{
                int count = iService.fixIssueAutomatic(datetime);
                System.out.println("" + count + " issues Fixed automatically");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    conn.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
   
}
