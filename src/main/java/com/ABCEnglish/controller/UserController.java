package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.request.StatusRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;  // Import để sử dụng @Slf4j
import org.springframework.web.bind.annotation.*;

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
}
