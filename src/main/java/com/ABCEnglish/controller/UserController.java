package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.UserDeleteResponse;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;  // Import để sử dụng @Slf4j
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @GetMapping()
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        Page<UserResponse> result = userService.getAllUsers(pageable,introspectRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping()
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid RegisterRequest request) throws MessagingException {
        log.info("Received request to register user: {}", request);

        try {
            UserResponse userResponse = userService.createUser(request);
            log.info("User created successfully: {}", userResponse);
            return ApiResponse.<UserResponse>builder()
                    .result(userResponse)
                    .build();
        } catch (MessagingException e) {
            log.error("Error occurred while sending verification email", e);
            throw e;  // Re-throw the exception if you want it to be handled by a global exception handler
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody UserRequest userRequest
    ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        UserResponse result = userService.updateUser(userRequest,introspectRequest);
        return ApiResponse.<UserResponse>builder().result(result).build();
    }


    @DeleteMapping("/{userId}")
    public ApiResponse<UserDeleteResponse> deleteUser(
            @PathVariable Integer userId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        UserDeleteResponse result = userService.deleteUser(userId,introspectRequest);
        return ApiResponse.<UserDeleteResponse>builder().result(result).build();
    }

    @PostMapping("/{userId}/verify")
    public Boolean updateStatus(
            @PathVariable Integer userId,
            @RequestBody StatusRequest request
    ){
        userService.updateStatus(userId,request);
        return true;
    }
    @GetMapping("/profile")
    public ApiResponse<UserResponse> getUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        UserResponse result = userService.getUser(introspectRequest);
        return ApiResponse.<UserResponse>builder().result(result).build();
    }
    @GetMapping("/admin/users")
    public ResponseEntity<Page<UserResponse>> getAllSocial(Pageable pageable, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        Page<UserResponse> result = userService.getAllUsers(pageable,introspectRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/ban")
    public ResponseEntity<String> banUser(@RequestBody PhoneRequest phone) {
        userService.banUserFor24Hours(phone.getPhone());
        return ResponseEntity.ok("User banned for 24 hours");
    }

    @PutMapping("/update-permission/{userId}")
    public ApiResponse<UserResponse> updatePermission(
            @PathVariable Integer userId,
            @RequestBody RoleUpdate roleId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        UserResponse result = userService.updatePermission(userId,introspectRequest,roleId.getRoleId());
        return ApiResponse.<UserResponse>builder().result(result).build();
    }
}