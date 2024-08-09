package com.codeBuffer.securitydemo.Repository;

import com.codeBuffer.securitydemo.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken,Long > {
      PasswordResetToken findByToken(String token);
}
