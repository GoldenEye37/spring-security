package com.codeBuffer.securitydemo.event;

import com.codeBuffer.securitydemo.entity.User;
import com.codeBuffer.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationCompleteEventListener{

    private final UserService userService;


    @EventListener
    public void onRegistrationCompleteEvent(RegistrationCompleteEvent event) {
        // create verification token to User with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // send mail to user
        String  url =
                event.getApplicationUrl()
                    + "/verifyRegistration?token="
                    +token;

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }
}
