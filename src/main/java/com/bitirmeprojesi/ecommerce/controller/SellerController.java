package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.config.JwtProvider;
import com.bitirmeprojesi.ecommerce.domain.AccountStatus;
import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.model.SellerReport;
import com.bitirmeprojesi.ecommerce.model.VerificationCode;
import com.bitirmeprojesi.ecommerce.repository.VerificationCodeRepository;
import com.bitirmeprojesi.ecommerce.request.LoginRequest;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.response.AuthResponse;
import com.bitirmeprojesi.ecommerce.service.EmailService;
import com.bitirmeprojesi.ecommerce.service.ISellerService;
import com.bitirmeprojesi.ecommerce.service.impl.AuthService;
import com.bitirmeprojesi.ecommerce.utils.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final ISellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest request) {
        String otp = request.getOtp();
        String email = request.getEmail();

//        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
//        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
//            throw new RuntimeException("Verification code not found or does not match");
//        }

        request.setEmail("seller_"+email);
        request.setOtp(otp);
        AuthResponse authResponse = authService.signIn(request);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerMail(@PathVariable("otp") String otp) {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new RuntimeException("OTP verification failed");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return ResponseEntity.ok(seller);
    }

    @PostMapping("/create-seller")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "email verification code";
        String text = "verifiy your email address using this link ";
        String frontendUrl = "http://localhost:8080/verify-seller";
        emailService.sendVerificationOtpMail(seller.getEmail(), verificationCode.getOtp(), subject, text+frontendUrl);

        return new ResponseEntity<>(seller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable("id") Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) {
//        String email = jwtProvider.getEmailFromJwtToken(jwt);
//        Seller seller = sellerService.getSellerByEmail(email);
//        SellerReport sellerReport = sellerReportService.getSellerReport(seller);
//        return new ResponseEntity<>(sellerReport, HttpStatus.OK);
//    }

    @GetMapping("/get-all-sellers")
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping("/update-seller")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(),seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable("id") Long id) throws SellerException {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
