package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String username;
    String fullname;

    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$", message = "must be a well-formed email address")
    @NotBlank(message = "Email is not blank")
    String email;

    String phone;
    String description;

    @NotBlank(message = "Password is not blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[a-zA-Z\\d@$!%*?&]{8,}", message = "Password must contain uppercase letters, lowercase letters, numbers and special characters")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;
}
