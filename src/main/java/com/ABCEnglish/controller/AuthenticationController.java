package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.AuthenticationResponse;
import com.ABCEnglish.dto.response.IntrospectResponse;
import com.ABCEnglish.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws KeyLengthException {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }


    @PostMapping("/token")
    ApiResponse<IntrospectResponse> introspectResponseApiResponse(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspectToken(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/requestresetpassword")
    public ApiResponse<String> resetPassword(@RequestBody EmailRequest request){
        var res = authenticationService.sendEmialResetPassword(request.getEmail());
        return ApiResponse.<String>builder()
                .result(res)
                .build();
    }
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request)  {
        var res = authenticationService.resetPassword(request);
        return ApiResponse.<String>builder()
                .result(res)
                .build();
    }
}
