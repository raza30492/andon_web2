/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem.resources;

import in.andonsystem.services.ForgotPasswordService;
import in.andonsystem.services.OTP;
import in.andonsystem.services.SMSService;
import in.andonsystem.services.UserService;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Md Zahid Raza
 */
@Path("/forgot_passwd")
public class ForgotPasswordResource2 {
    /*
    @POST
    @Path("/send_otp")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendOTP(@FormParam("userId") int userId){
        UserService uService = new UserService();
        ForgotPasswordService fService = new ForgotPasswordService();
        Boolean result = uService.findUser(userId);
        Boolean status = false;
        if(result){
            OTP otpObj = new OTP(6);
            String otp = otpObj.nextString();
            fService.recordForgotPassword(Integer.parseInt(otp), userId);
            String mobile = uService.getUserMobile(userId);
            
            //Send otp           
            String message = "OTP to reset password in ANDON SYSTEM APPLICATION is: " + otp;
            status = SMSService.sendSMS(mobile, message);
       
        }
        return ( status ? "success" : "fail");
    }
    
    @POST
    @Path("/verify_otp")
    @Produces(MediaType.TEXT_PLAIN)
    public String verifyOTP(@FormParam("userId") int userId,@FormParam("otp") int otp){
        ForgotPasswordService fService = new ForgotPasswordService();
        Boolean result = fService.verifyOTP(userId, otp);
        fService.closeConnection();
        return ( result ? "success" : "fail");
    }
    
    @POST
    @Path("/change_password")
    @Produces(MediaType.TEXT_PLAIN)
    public String changePassword(@FormParam("userId") int userId,@FormParam("newPassword") String newPassword){
        UserService uService = new UserService();
        Boolean result = uService.changePassword(userId, newPassword);
        uService.closeConnection();
        return ( result ? "success" : "fail");
    }
*/
}
