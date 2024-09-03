package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.Role;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.mapper.UserMapper;
import com.ABCEnglish.reponsesitory.RoleRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;


    public UserResponse createUser(RegisterRequest request){
        Role studentRole = roleRepository.findByName("Student");
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(studentRole);
        return userMapper.toUserResponse(user);
    }
}
