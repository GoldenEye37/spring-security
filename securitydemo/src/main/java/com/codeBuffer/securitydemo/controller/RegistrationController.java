package com.codeBuffer.securitydemo.controller;


import com.codeBuffer.securitydemo.entity.User;
import com.codeBuffer.securitydemo.entity.VerificationToken;
import com.codeBuffer.securitydemo.event.RegistrationCompleteEvent;
import com.codeBuffer.securitydemo.model.GenericResponse;
import com.codeBuffer.securitydemo.model.PasswordModel;
import com.codeBuffer.securitydemo.model.UserModel;
import com.codeBuffer.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public GenericResponse registerUser(@RequestBody UserModel userModel,
                                        final HttpServletRequest request) {

        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return new GenericResponse(true, "Successfully registered.");
    }


    @GetMapping("/verifyRegistration")
    public GenericResponse verifyRegistration(@RequestParam("token") String token) {
        return userService.validateVerificationToken(token);

    }

    @GetMapping("/resendVerifyToken")
    public GenericResponse resendVerificationToken(@RequestParam("token") String oldToken,
                                                   HttpServletRequest request) {

        VerificationToken verificationToken
                = userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl((request)), verificationToken);
        return new GenericResponse(true, "Verification Link Sent");
    }

    private void resendVerificationTokenMail(User user,
                                             String applicationUrl, VerificationToken verificationToken) {

        // send mail to user
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + verificationToken.getToken();

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }

    @PostMapping("/resetPassword")
    public GenericResponse resetPassword(@RequestBody PasswordModel passwordModel,
                                         HttpServletRequest request) {

        User user = userService.findUserByEmail(passwordModel.getEmail());

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        String url = passwordResetTokenMail(user, applicationUrl((request)),
                token);


        return new GenericResponse(true, url);
    }

    @PostMapping("/changePassword")
    public GenericResponse changePassword(@RequestBody PasswordModel passwordModel) {
        GenericResponse response = new GenericResponse();
        User user = userService.findUserByEmail(passwordModel.getEmail());

        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
            response.setSuccess(false);
            response.setMessage("Invalid Old Password");
        } else {
            userService.changePassword(user, passwordModel.getNewPassword());
            response.setSuccess(true);
            response.setMessage("Password Reset Request Successful");
        }

        return response;
    }

    private String passwordResetTokenMail(User user, String applicationUrl,
                                          String token) {

        // send mail to user
        String url =
                applicationUrl
                        + "/savePassword?token="
                        + token;

        //sendVerificationEmail()
        log.info("Click the link to reset your password: {}", url);
        return url;
    }

    @PostMapping("/savePassword")
    public GenericResponse savePassword(@RequestParam("token") String token,
                                        @RequestBody PasswordModel passwordModel) {
        userService.validatePasswordResetToken(token);
        User user = userService.getUserByPasswordResetToken(token);
        userService.changePassword(user, passwordModel.getNewPassword());
        return new GenericResponse(true, "Operation has been successful.");
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }


}
