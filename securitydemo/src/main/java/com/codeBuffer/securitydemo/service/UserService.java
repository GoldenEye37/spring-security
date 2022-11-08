package com.codeBuffer.securitydemo.service;


import com.codeBuffer.securitydemo.Entity.User;
import com.codeBuffer.securitydemo.Entity.VerificationToken;
import com.codeBuffer.securitydemo.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
