package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User  toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);
    @AfterMapping
    default void addCode(@MappingTarget UserResponse userResponse, User user) {
        // Ví dụ: Tạo mã 'code' dựa trên userId
        String code = "USER" + user.getUserId();
        userResponse.setToken(code);
    }
}
