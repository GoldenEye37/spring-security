package com.codeBuffer.securitydemo.service.impl;

import com.codeBuffer.securitydemo.entity.PasswordResetToken;
import com.codeBuffer.securitydemo.entity.User;
import com.codeBuffer.securitydemo.entity.VerificationToken;
import com.codeBuffer.securitydemo.repository.PasswordResetTokenRepository;
import com.codeBuffer.securitydemo.repository.UserRepository;
import com.codeBuffer.securitydemo.repository.VerificationTokenRepository;
import com.codeBuffer.securitydemo.exception.ex.NotFoundException;
import com.codeBuffer.securitydemo.exception.ex.ExpiredException;
import com.codeBuffer.securitydemo.model.GenericResponse;
import com.codeBuffer.securitydemo.model.UserModel;
import com.codeBuffer.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setName(userModel.getName());
        user.setSurname(userModel.getSurname());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public GenericResponse validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Token %s is invalid.", token))
                );

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            throw new ExpiredException("Token Expired");
        }

        user.setEnabled(true);
        userRepository.save(user);
        return new GenericResponse(true, "User Verified Successfully.");
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Token %s is invalid.", oldToken))
                );

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
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
    }

    @Override
    public void validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Password reset token not found."));

        Calendar cal = Calendar.getInstance();

        if ((passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new ExpiredException("Reset Token Expired.");
        }
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(()-> new NotFoundException("No password reset token found."));
        return resetToken.getUser();
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

}
