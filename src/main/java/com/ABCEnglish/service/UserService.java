package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.UserDeleteResponse;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
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
        user.setBan24h(false);
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
    public UserResponse updateUser(UserRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(request,user);
        user.setUpdatedAt(new Date());
        User updateUser = userRepository.save(user);
        return userMapper.toUserResponse(updateUser);
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
        Role roleAdmin = roleRepository.findByName("admin")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        if (user.getRole().getName().equals(roleAdmin.getName())) {
            Page<User> users = userRepository.findAll(pageable);
            return users.map(userMapper::toUserResponse);
        }
        throw new AppException(ErrorCode.FORBIDDEN);
    }


    @Transactional
    public void banUserFor24Hours(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setBan24h(true);
        user.setBanUntil(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // +24h
        userRepository.save(user);
    }

    @Scheduled(fixedRate = 3600000) // Mỗi giờ kiểm tra
    public boolean resetBanStatus() {
        List<User> bannedUsers = userRepository.findAllByBan24hTrueAndBanUntilBefore(new Date());
        for (User user : bannedUsers) {
            user.setBan24h(false);
            user.setBanUntil(null);
            userRepository.save(user);
        }
        return false;
    }
    public boolean isAdmin(Integer userId) {
        // Lấy thông tin User từ database
        Optional<User> userOptional = userRepository.findById(userId);

        // Kiểm tra vai trò ADMIN
        return userOptional.isPresent() &&
                "ADMIN".equalsIgnoreCase(userOptional.get().getRole().getName());
    }

    public UserDeleteResponse deleteUser(Integer userDeleteId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(isAdmin(userId)){
            User userDelete = userRepository.findById(userDeleteId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            userDelete.setStatus(false);
            UserDeleteResponse response = new UserDeleteResponse();
            response.setUserId(userDeleteId);
            response.setMessage("delete success");
            response.setStatus(true);
            return ApiResponse.<UserDeleteResponse>builder().result(response).build().getResult();
        }
        throw new AppException(ErrorCode.ACCESS_DENIED);
    }


}