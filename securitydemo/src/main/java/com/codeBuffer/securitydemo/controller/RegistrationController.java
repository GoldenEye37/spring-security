package com.codeBuffer.securitydemo.controller;


import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.Entity.VerificationToken;
import com.codeBuffer.securitydemo.Event.RegistrationCompleteEvent;
import com.codeBuffer.securitydemo.model.PasswordModel;
import com.codeBuffer.securitydemo.model.UserModel;
import com.codeBuffer.securitydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel,
                               final HttpServletRequest request){

        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }


    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User Verifies Successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request){

        VerificationToken verificationToken
                = userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl((request)), verificationToken);
        return "Verification Link Sent";
    }

    private void resendVerificationTokenMail(User user,
                                             String applicationUrl, VerificationToken verificationToken) {

        // send mail to user
        String  url =
                applicationUrl
                        + "/verifyRegistration?token="
                        +verificationToken.getToken();

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,
                                HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user!=null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl((request)),
                    token);
        }

        return url;
    }

    private String passwordResetTokenMail(User user, String applicationUrl,
                                          String token) {

        // send mail to user
        String  url =
                applicationUrl
                        + "/savePassword?token="
                        +token;

        //sendVerificationEmail()
        log.info("Click the link to reser your password: {}",
                url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+
                request.getServerName()+
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }


}
