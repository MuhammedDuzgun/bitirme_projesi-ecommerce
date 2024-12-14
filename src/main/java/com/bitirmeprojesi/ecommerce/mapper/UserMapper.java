package com.bitirmeprojesi.ecommerce.mapper;

import com.bitirmeprojesi.ecommerce.dto.UserDto;
import com.bitirmeprojesi.ecommerce.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
