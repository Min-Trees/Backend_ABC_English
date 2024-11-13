package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CommentRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CommentDeleteResponse;
import com.ABCEnglish.dto.response.CommentResponse;
import com.ABCEnglish.service.CommentService;
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
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping("/{socialId}")
    public ApiResponse<CommentResponse> addComment(
            @RequestBody CommentRequest request,
            @PathVariable Integer socialId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        CommentResponse result = commentService.createComment(request,socialId,introspectRequest);
        return ApiResponse.<CommentResponse>builder().result(result).build();
    }

    @PutMapping("/{socialId}/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @RequestBody CommentRequest request,
            @PathVariable Integer socialId,
            @PathVariable Integer commentId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        CommentResponse result = commentService.updateComment(request,socialId,commentId,introspectRequest);
        return ApiResponse.<CommentResponse>builder().result(result).build();
    }

    @GetMapping()
    public ResponseEntity<Page<CommentResponse>> getAllComment(Pageable pageable){
        Page<CommentResponse> commentsResponse = commentService.getAllComment(pageable);
        return ResponseEntity.ok(commentsResponse);
    }

    @GetMapping("/{socialId}/{commentId}")
    public ApiResponse<CommentResponse> getComment(
            @PathVariable Integer socialId,
            @PathVariable Integer commentId ) {
        CommentResponse result = commentService.getComment(socialId,commentId);
        return ApiResponse.<CommentResponse>builder().result(result).build();
    }

    @DeleteMapping("/{socialId}/{commentId}")
    public CommentDeleteResponse deleteComment(
            @PathVariable Integer socialId,
            @PathVariable Integer commentId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return commentService.deleteResponse(socialId,commentId,introspectRequest);
    }
}
