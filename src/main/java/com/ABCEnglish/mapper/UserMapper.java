package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User  toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);
}
