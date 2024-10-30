package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.SocialRequest;
import com.ABCEnglish.dto.response.SocialResponse;
import com.ABCEnglish.service.SocialService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocialController {
    SocialService socialService;
    @PostMapping()
     public ApiResponse<SocialResponse> addSocial(
            @RequestBody SocialRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        SocialResponse result = socialService.createPost(request,introspectRequest);
        return ApiResponse.<SocialResponse>builder().result(result).build();
    }
    @PutMapping("/{socialId}")
    public ApiResponse<SocialResponse> updateSocial(
            @RequestBody SocialRequest request,
            @PathVariable Integer socialId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        SocialResponse result = socialService.updatePost(socialId,request,introspectRequest);
        return ApiResponse.<SocialResponse>builder().result(result).build();
    }

    @GetMapping("/{socialId}")
    public ApiResponse<SocialResponse> getSocial(
            @PathVariable Integer socialId){
        SocialResponse result = socialService.getSocial(socialId);
        return ApiResponse.<SocialResponse>builder().result(result).build();
    }

    @GetMapping()
    public ResponseEntity<Page<SocialResponse>> getAllSocial(Pageable pageable){
        Page<SocialResponse> socialResponses = socialService.getAllSocial(pageable);
        return ResponseEntity.ok(socialResponses);
    }

}
