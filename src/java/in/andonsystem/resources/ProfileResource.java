/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem.resources;

import in.andonsystem.DBase;
import in.andonsystem.services.UserService;
import java.sql.Connection;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Md Zahid Raza
 */
@Path("/profile")
public class ProfileResource {
    
    @POST
    @Path("/change_email")
    @Produces(MediaType.TEXT_PLAIN)
    public String changeEmail(@QueryParam("authToken") String authToken,@QueryParam("email") String email){
        Connection conn = DBase.getConn();
        UserService uService = new UserService(conn);
        Boolean result = false;
        try{
            result = uService.changeEmail(authToken, email);
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
    @Path("/change_mobile")
    @Produces(MediaType.TEXT_PLAIN)
    public String changeMobile(@QueryParam("authToken") String authToken,@QueryParam("mobile") String mobile){
        Connection conn = DBase.getConn();
        UserService uService = new UserService(conn);
        Boolean result = false;
        try{
            result = uService.changeMobile(authToken, mobile);
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
    @Path("/change_password")
    @Produces(MediaType.TEXT_PLAIN)
    public String changePassword(@QueryParam("authToken") String authToken,@QueryParam("currPassword") String currPasswd,@QueryParam("newPassword") String newPasswd){
        Connection conn = DBase.getConn();
        UserService uService = new UserService(conn);
        Boolean result = false;
        try{
        result = uService.changePassword(authToken, currPasswd, newPasswd);
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
    
    
}
