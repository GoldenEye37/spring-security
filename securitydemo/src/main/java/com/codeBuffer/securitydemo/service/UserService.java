package com.codeBuffer.securitydemo.service;


import com.codeBuffer.securitydemo.entity.User;
import com.codeBuffer.securitydemo.entity.VerificationToken;
import com.codeBuffer.securitydemo.model.GenericResponse;
import com.codeBuffer.securitydemo.model.UserModel;

public interface UserService {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    GenericResponse validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    void validatePasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
