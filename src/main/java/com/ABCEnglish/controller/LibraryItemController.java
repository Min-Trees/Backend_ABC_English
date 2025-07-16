package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.LibraryDocRequest;
import com.ABCEnglish.dto.request.LibraryQuestionRequest;
import com.ABCEnglish.dto.response.LibraryDocResponse;
import com.ABCEnglish.dto.response.LibraryQuestionDeleteResponse;
import com.ABCEnglish.dto.response.LibraryQuestionResponse;
import com.ABCEnglish.entity.LibraryDoc;
import com.ABCEnglish.service.LibraryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/library-item")
@RequiredArgsConstructor
public class LibraryItemController {
    private final LibraryItemService libraryItemService;

    @PostMapping("/question")
    public LibraryQuestionResponse addLibraryItem(
            @RequestBody LibraryQuestionRequest libraryQuestionRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.addQuestionToLibrary(libraryQuestionRequest,token);
    }
    
    @PostMapping("/doc")
    public LibraryDocResponse addLibraryDoc(
            @RequestBody LibraryDocRequest libraryDocRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.addDocToLibrary(libraryDocRequest,token);
    }
    @PutMapping("/update/{libraryQuestionId}")
    public LibraryQuestionResponse updateLibraryItem(
            @PathVariable Integer libraryQuestionId,
            @RequestBody LibraryQuestionRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.updateQuestionToLibrary(libraryQuestionId,request,token);
    }
    @GetMapping("/{libraryQuestionId}")
    public LibraryQuestionResponse getLibraryItem(
            @PathVariable Integer libraryQuestionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.getQuestionFromLibrary(libraryQuestionId,token);
    }

    @DeleteMapping("/{libraryQuestionId}")
    public LibraryQuestionDeleteResponse deleteLibraryItem(
            @PathVariable Integer libraryQuestionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.deleteQuestionFromLibrary(libraryQuestionId,token);
    }

    @GetMapping("/library/{libraryId}/questions")
    public ResponseEntity<Page<LibraryQuestionResponse>> getLibraryQuestions(
            @PathVariable Integer libraryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        Pageable pageable = PageRequest.of(page, size);
        Page<LibraryQuestionResponse> responses = libraryItemService.getQuestionsFromLibrary(libraryId, pageable, token);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/get-doc/{libraryDocId}")
    public LibraryDocResponse getLibraryDoc(
            @PathVariable Integer libraryDocId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION)String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.getDoc(libraryDocId,token);
    }
}
