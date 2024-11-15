package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.request.StatusRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.UserMapper;
import com.ABCEnglish.reponsesitory.RoleRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.ABCEnglish.reponsesitory.VerificationTokenRepository;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationService authenticationService;
    public UserResponse createUser(RegisterRequest request) throws MessagingException {

        Role role = roleRepository.findByName("guest")
                .orElseGet(()-> {
                    Role newRole = new Role();
                    newRole.setName("guest");
                    return roleRepository.save(newRole);
                });
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user = userRepository.save(user);

        String token = VerificationToken.generateToken();
        VerifiTokenEntity verifiTokenEntity = new VerifiTokenEntity(user,token);
        verificationTokenRepository.save(verifiTokenEntity);
        String verificationCode = authenticationService.sendVerificationEmail(user,token);
        System.out.println(token);
        UserResponse userResponse =  userMapper.toUserResponse(user);
        userResponse.setToken(token);
        return userResponse;
    }

    public Boolean updateStatus(Integer userId,StatusRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        user.setStatus(true);
        userRepository.save(user);
        return true;
    }

    public UserResponse getUser(IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role roleAdmin = roleRepository.findByName("Admin")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        if (user.getRole().getName().equals(roleAdmin.getName())) {
            Page<User> users = userRepository.findAll(pageable);
            return users.map(userMapper::toUserResponse);
        }
        throw new AppException(ErrorCode.FORBIDDEN);
    }


}
