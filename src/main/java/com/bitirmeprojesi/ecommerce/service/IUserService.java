package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.User;

public interface IUserService {

    User findUserByJwtToken(String jwt);
    User findUserByEmail(String email);

}
