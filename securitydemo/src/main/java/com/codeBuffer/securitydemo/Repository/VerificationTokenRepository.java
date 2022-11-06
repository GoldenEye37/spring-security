package com.codeBuffer.securitydemo.Repository;

import com.codeBuffer.securitydemo.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationTokenRepository extends
        JpaRepository<VerificationToken, Long> {
}
