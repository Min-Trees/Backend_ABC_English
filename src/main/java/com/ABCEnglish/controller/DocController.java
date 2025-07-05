package com.ABCEnglish.controller;

import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.DocRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.DocDeleteResponse;
import com.ABCEnglish.dto.response.DocResponse;
import com.ABCEnglish.service.DocService;
import com.ABCEnglish.service.TokenService;
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
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocController {

    DocService docService;
    JwtUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping("/{lessonId}")
    public ApiResponse<DocResponse> addDoc(
            @RequestBody DocRequest request,
            @PathVariable Integer lessonId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{
         String token = authorizationHeader.startsWith("Bearer")
                 ? authorizationHeader.substring("Bearer".length()).trim()
                 : authorizationHeader;

        DocResponse result = docService.createDoc(lessonId,request,token);
        return ApiResponse.<DocResponse>builder().result(result).build();
    }

    @PutMapping("/{courseId}/{docId}")
    public ApiResponse<DocResponse> updateDoc(
            @RequestBody DocRequest request,
            @PathVariable Integer courseId,
            @PathVariable Integer docId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{

        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        DocResponse result = docService.updateDoc(courseId,docId,request,token);
        return ApiResponse.<DocResponse>builder().result(result).build();
    }

//    @GetMapping("/{lessonId}")
//    public ResponseEntity<Page<DocResponse>> getAllDoc(Pageable pageable, @PathVariable Integer lessonId){
//        Page<DocResponse> docResponses = docService.getAllDoc(pageable, lessonId);
//        return ResponseEntity.ok(docResponses);
//    }
    @GetMapping("/{lessonId}")
    public ResponseEntity<Page<DocResponse>> getDocById(@PathVariable Integer lessonId,Pageable pageable){
        Page<DocResponse> docResponses = docService.getByLesson(lessonId,pageable);
        return ResponseEntity.ok(docResponses);
    }

    @GetMapping("/{courseId}/{docId}")
    public ApiResponse<DocResponse> getDoc(
            @PathVariable Integer courseId,
            @PathVariable Integer docId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{

        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        DocResponse result = docService.getDoc(courseId,docId,token);
        return ApiResponse.<DocResponse>builder().result(result).build();
    }
    @DeleteMapping("/{courseId}/{docId}")
    public DocDeleteResponse deleteDocResponse(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer docId,
            @PathVariable Integer courseId) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        return docService.deleteDoc(courseId,docId, token);
    }


}
