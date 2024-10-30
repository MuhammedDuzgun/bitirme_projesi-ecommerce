package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.domain.USER_ROLE;
import com.bitirmeprojesi.ecommerce.request.LoginRequest;
import com.bitirmeprojesi.ecommerce.response.AuthResponse;
import com.bitirmeprojesi.ecommerce.response.SignupRequest;

public interface IAuthService {

    void sendLoginOtp(String email, USER_ROLE role);
    String createUser(SignupRequest request);
    AuthResponse signIn(LoginRequest request);

}
