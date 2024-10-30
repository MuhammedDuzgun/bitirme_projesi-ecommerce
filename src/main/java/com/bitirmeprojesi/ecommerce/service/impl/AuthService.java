package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.config.JwtProvider;
import com.bitirmeprojesi.ecommerce.domain.USER_ROLE;
import com.bitirmeprojesi.ecommerce.model.Cart;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.model.User;
import com.bitirmeprojesi.ecommerce.model.VerificationCode;
import com.bitirmeprojesi.ecommerce.repository.ICartRepository;
import com.bitirmeprojesi.ecommerce.repository.ISellerRepository;
import com.bitirmeprojesi.ecommerce.repository.IUserRepository;
import com.bitirmeprojesi.ecommerce.repository.VerificationCodeRepository;
import com.bitirmeprojesi.ecommerce.request.LoginRequest;
import com.bitirmeprojesi.ecommerce.response.AuthResponse;
import com.bitirmeprojesi.ecommerce.response.SignupRequest;
import com.bitirmeprojesi.ecommerce.service.CustomUserDetailsServiceImpl;
import com.bitirmeprojesi.ecommerce.service.EmailService;
import com.bitirmeprojesi.ecommerce.service.IAuthService;
import com.bitirmeprojesi.ecommerce.utils.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final ISellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtProvider jwtProvider;

    @Override
    public void sendLoginOtp(String email, USER_ROLE role) {
        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_CUSTOMER)) {
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new UsernameNotFoundException("User not found with email: " + email);
                }
            } else {
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null) {
                    throw new UsernameNotFoundException("Seller not found with email: " + email);
                }
            }
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode != null) {
            verificationCodeRepository.delete(verificationCode);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(otp);
        verificationCode1.setEmail(email);
        verificationCodeRepository.save(verificationCode1);

        String subject = "OTP verification";
        String text = "OTP verification code: " + verificationCode1.getOtp();

        emailService.sendVerificationOtpMail(email, otp, subject, text);

    }

    @Override
    public String createUser(SignupRequest request) {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Verification code is invalid");
        }

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            User createUser = new User();
            createUser.setEmail(request.getEmail());
            createUser.setFullName(request.getFullName());
            createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createUser.setPhone("12345");
            createUser.setPassword(passwordEncoder.encode(request.getOtp()));

            //create cart
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);

            user = userRepository.save(createUser);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signIn(LoginRequest request) {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }

        if (userDetails == null) {
            throw new BadCredentialsException("Username or password is incorrect");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong verification code");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
