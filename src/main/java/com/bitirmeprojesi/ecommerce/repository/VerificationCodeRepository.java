package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByEmail(String email);

}
