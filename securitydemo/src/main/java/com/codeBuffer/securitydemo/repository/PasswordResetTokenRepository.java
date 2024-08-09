package com.codeBuffer.securitydemo.repository;

import com.codeBuffer.securitydemo.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long > {
      Optional<PasswordResetToken> findByToken(String token);
}
