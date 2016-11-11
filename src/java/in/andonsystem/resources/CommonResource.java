/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem.resources;

import in.andonsystem.DBase;
import in.andonsystem.models.AuthData;
import in.andonsystem.models.InitData1;
import in.andonsystem.models.Preferences;
import in.andonsystem.models.RelaunchData;
import in.andonsystem.models.User;
import in.andonsystem.services.DeptService;
import in.andonsystem.services.DesignationService;
import in.andonsystem.services.IssueService;
import in.andonsystem.services.MiscService;
import in.andonsystem.services.ProblemService;
import in.andonsystem.services.SectionService;
import in.andonsystem.services.UserService;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Administrator
 */
@Path("/res")
public class CommonResource {
    @Context 
    ServletContext context;

    //is the application re-launched 
    @GET
    @Path("/relaunch")
    @Produces(MediaType.APPLICATION_JSON)
    public RelaunchData isRelaunched(@QueryParam("time") long time){
        
        MiscService mService = new MiscService();
        
        String launchString = mService.readProperties(context,Preferences.APP_LAUNCH);
        long launchTime = Long.parseLong(launchString, 10);
        String code = (time < launchTime ? "YES" : "NO");
        RelaunchData data = new RelaunchData(
                code,
                Long.parseLong(mService.readProperties(context, Preferences.APP_LAUNCH)), 
                System.currentTimeMillis()
        );
        return data;
        
    }
    
    @GET
    @Path("init")
    @Produces(MediaType.APPLICATION_JSON)
    public InitData1 initialize(){
        Connection conn = DBase.getConn();
        MiscService mService = new MiscService();
        SectionService sService = new SectionService(conn);
        DeptService dService = new DeptService(conn);
        ProblemService pService = new ProblemService(conn);
        IssueService iService = new IssueService(conn);
        DesignationService desgnService = new DesignationService(conn);
        UserService uService = new UserService(conn);
        
        InitData1 data = null;
        try{
        
            long launchTime = Long.parseLong(mService.readProperties(context,Preferences.APP_LAUNCH));
            int timeAck = Integer.parseInt((String)context.getAttribute("time_ack"));
            int timeLevel1 = Integer.parseInt((String)context.getAttribute("time_level1"));
            int timeLevel2 = Integer.parseInt((String)context.getAttribute("time_level2"));
            String lineStr = mService.readProperties(context,Preferences.LINES);
            int lines = 0;
            if(lineStr != null)
                lines = Integer.parseInt(lineStr);
            data = new InitData1(
                    launchTime,
                    System.currentTimeMillis(),
                    timeAck,
                    timeLevel1,
                    timeLevel2,
                    lines,
                    sService.getSections(),
                    dService.getDepartments(),
                    pService.getProblems(),
                    iService.getIssues1(),
                    uService.getUsers(),
                    desgnService.getDesgns(),
                    desgnService.getDesgnLineMapping(),
                    desgnService.getDesgnProblemMapping()
            );
            //new InitData(launchTime, launchTime, lines, sections, departments, problems, issues);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                conn.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return data;
    }

    @POST
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthData authenticate(@QueryParam("userId") int userId,@QueryParam("password") String password){
        Connection conn = DBase.getConn();
        UserService uService = new UserService(conn);
        AuthData result = null;
        try{
            User user = uService.authUser(userId, password);
            
            if(user != null){
                result = new AuthData("success", user);
            }else{
                result = new AuthData("fail", user);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                conn.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    @GET
    @Path("/current_time")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCurrentTime(){ 
        return String.valueOf(System.currentTimeMillis());
    }
}
