package com.codeBuffer.securitydemo.event;

import com.codeBuffer.securitydemo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class RegistrationCompleteEvent {
    private User user;
    private String applicationUrl;
}
