/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem.resources;

import in.andonsystem.DBase;
import in.andonsystem.models.AuthData;
import in.andonsystem.models.Preferences;
import in.andonsystem.models.RelaunchData;
import in.andonsystem.models.User;
import in.andonsystem.services.MiscService;
import in.andonsystem.services.SectionService;
import in.andonsystem.services.UserService;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Md Zahid Raza
 */
@Path("/misc")
public class MiscResource {
    @Context
    private ServletContext context;
    
    @POST
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthData authenticate(@FormParam("userId") int userId,@FormParam("password") String password){
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
    @Path("/relaunch")
    @Produces(MediaType.APPLICATION_JSON)
    public RelaunchData isRelaunched(@QueryParam("time") long time,@QueryParam("version") String version){
        MiscService mService = new MiscService();
        
        String launchString = mService.readProperties(context,Preferences.APP_LAUNCH);
        long launchTime = Long.parseLong(launchString, 10);
        String relaunched = (time < launchTime ? "YES" : "NO");
        String updateApp = (checkAppUpdate(version) ? "YES" : "NO");
        RelaunchData data = new RelaunchData(
                relaunched,
                updateApp,
                Long.parseLong(mService.readProperties(context, Preferences.APP_LAUNCH)), 
                System.currentTimeMillis()
        );
        
        return data;
        
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/set_relaunched")
    public String setAppRelaunched(){
        long timeNow = System.currentTimeMillis();
        new MiscService().writeProperties(context, Preferences.APP_LAUNCH, String.valueOf(timeNow));
        return "success";
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/add_section")
    public String addSection(@QueryParam("name") String name){
        Connection conn = DBase.getConn();
        
        SectionService sService = new SectionService(conn);
        Boolean result = false;
        try{
            result = sService.addSection(name);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                conn.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return (result ? "success" : "fail");
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/set_start_time")
    public String setStartTime(@QueryParam("hour") int hour,@QueryParam("minute") int minute){
        MiscService mService = new MiscService();
        mService.writeProperties(context, Preferences.START_HOUR, String.valueOf(hour));
        mService.writeProperties(context, Preferences.START_MINUTE, String.valueOf(minute));
        context.setAttribute("start_hour", String.valueOf(hour));
        context.setAttribute("start_minute", String.valueOf(minute));
        return "success";
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/set_end_time")
    public String setEndTime(@QueryParam("hour") int hour,@QueryParam("minute") int minute){
        MiscService mService = new MiscService();
        mService.writeProperties(context, Preferences.END_HOUR, String.valueOf(hour));
        mService.writeProperties(context, Preferences.END_MINUTE, String.valueOf(minute));
        context.setAttribute("end_hour", String.valueOf(hour));
        context.setAttribute("end_minute", String.valueOf(minute));
        return "success";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/check_update")
    public Boolean isUpdateAvailable(@QueryParam("version") String version){
        MiscService mService = new MiscService();
        String verStr = mService.readProperties(context, Preferences.APP_VERSION);
        String[] v1 = verStr.split("-");
        //int currVer=0;
        int currVer = (100*Integer.parseInt(v1[0])) + (10*Integer.parseInt(v1[1])) + Integer.parseInt(v1[2]);
        
        String[] v2 = version.split("-");
        int oldVer = (100*Integer.parseInt(v2[0])) + (10*Integer.parseInt(v2[1])) + Integer.parseInt(v2[2]);
        
        if(currVer > oldVer)
            return true;
        else
            return false;
    }
    
    private Boolean checkAppUpdate(String version){
        MiscService mService = new MiscService();
        String verStr = mService.readProperties(context, Preferences.APP_VERSION);
        String[] v1 = verStr.split("-");
        //int currVer=0;
        int currVer = (100*Integer.parseInt(v1[0])) + (10*Integer.parseInt(v1[1])) + Integer.parseInt(v1[2]);
        
        String[] v2 = version.split("-");
        int oldVer = (100*Integer.parseInt(v2[0])) + (10*Integer.parseInt(v2[1])) + Integer.parseInt(v2[2]);
        
        if(currVer > oldVer)
            return true;
        else
            return false;
    }
    
}
