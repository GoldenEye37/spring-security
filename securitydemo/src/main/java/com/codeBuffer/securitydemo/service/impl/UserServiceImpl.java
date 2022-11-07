package com.codeBuffer.securitydemo.service.impl;

import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.Entity.VerificationToken;
import com.codeBuffer.securitydemo.Repository.UserRepository;
import com.codeBuffer.securitydemo.Repository.VerificationTokenRepository;
import com.codeBuffer.securitydemo.model.UserModel;
import com.codeBuffer.securitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setName(userModel.getName());
        user.setSurname(userModel.getSurname());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken
                = new VerificationToken(user, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0)  {
            verificationTokenRepository.delete(verificationToken);
            return "Expired";
        }



        return null;
    }
}
