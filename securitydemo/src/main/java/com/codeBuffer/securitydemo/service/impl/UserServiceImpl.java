package com.codeBuffer.securitydemo.service.impl;

import com.codeBuffer.securitydemo.Entity.PasswordResetToken;
import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.Entity.VerificationToken;
import com.codeBuffer.securitydemo.Repository.PasswordResetTokenRepository;
import com.codeBuffer.securitydemo.Repository.UserRepository;
import com.codeBuffer.securitydemo.Repository.VerificationTokenRepository;
import com.codeBuffer.securitydemo.model.UserModel;
import com.codeBuffer.securitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

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

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(oldToken);

        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken
                = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken
                = passwordResetTokenRepository.findByToken(token);

        if(passwordResetToken == null){
            return "invalid";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if((passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0)  {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "Expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
