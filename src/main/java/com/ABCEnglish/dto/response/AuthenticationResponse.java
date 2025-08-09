package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    private String token;          // accessToken
    private String refreshToken;
    boolean authenticated;
    Role role;
    Integer userId;
}
