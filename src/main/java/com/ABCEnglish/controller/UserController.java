package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.SocialResponse;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.service.UserService;
import com.google.protobuf.Api;
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
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{userId}/verifi")
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
}