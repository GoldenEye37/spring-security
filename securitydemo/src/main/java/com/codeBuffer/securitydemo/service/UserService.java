package com.codeBuffer.securitydemo.service;


import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);
}
