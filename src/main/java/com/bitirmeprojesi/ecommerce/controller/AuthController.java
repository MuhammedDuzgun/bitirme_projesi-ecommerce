package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.domain.USER_ROLE;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.model.VerificationCode;
import com.bitirmeprojesi.ecommerce.repository.IUserRepository;
import com.bitirmeprojesi.ecommerce.request.LoginOtpRequest;
import com.bitirmeprojesi.ecommerce.request.LoginRequest;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.response.AuthResponse;
import com.bitirmeprojesi.ecommerce.response.SignupRequest;
import com.bitirmeprojesi.ecommerce.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody SignupRequest signupRequest) {
        String jwt = authService.createUser(signupRequest);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("register success");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@RequestBody LoginOtpRequest request) {
        authService.sendLoginOtp(request.getEmail(), request.getRole());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("send otp success");

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.signIn(request);
        return ResponseEntity.ok(authResponse);
    }

}
