package com.codeBuffer.securitydemo.Entity.listner;

import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.Event.RegistrationCompleteEvent;
import com.codeBuffer.securitydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.EventListener;
import java.util.UUID;


@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create verification token to User with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // send mail to user
        String  url =
                event.getApplicationUrl()
                    + "verifyRegistration?token="
                    +token;

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }
}
