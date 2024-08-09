package com.codeBuffer.securitydemo.service;


import com.codeBuffer.securitydemo.entity.User;
import com.codeBuffer.securitydemo.entity.VerificationToken;
import com.codeBuffer.securitydemo.model.UserModel;

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
