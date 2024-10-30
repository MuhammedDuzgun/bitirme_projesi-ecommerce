package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.config.JwtProvider;
import com.bitirmeprojesi.ecommerce.domain.AccountStatus;
import com.bitirmeprojesi.ecommerce.domain.USER_ROLE;
import com.bitirmeprojesi.ecommerce.exception.SellerException;
import com.bitirmeprojesi.ecommerce.model.Address;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.repository.IAddressRepository;
import com.bitirmeprojesi.ecommerce.repository.ISellerRepository;
import com.bitirmeprojesi.ecommerce.service.ISellerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SellerService implements ISellerService {

    private final ISellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null) {
            throw new RuntimeException("seller already exists");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id)
                .orElseThrow(()-> new SellerException("seller not found"));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new RuntimeException("seller not found with email: " + email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus accountStatus) {
        return sellerRepository.findByAccountStatus(accountStatus);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) {
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("seller not found with id: " + id));

        if (seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }

        if (seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }

        if (seller.getBankDetails() != null
            && seller.getBankDetails().getAccountHolderName() != null
            && seller.getBankDetails().getIfscCode() != null
            && seller.getBankDetails().getAccountNumber() != null
        ) {
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }

        if (seller.getPickupAddress() != null
            && seller.getPickupAddress().getAddress() != null
            && seller.getPickupAddress().getMobile() != null
            && seller.getPickupAddress().getCity() != null
            && seller.getPickupAddress().getState() != null
        ) {
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }

        if (seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws SellerException {
        Seller Seller = getSellerById(id);
        sellerRepository.delete(Seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(accountStatus);
        return sellerRepository.save(seller);
    }
}
