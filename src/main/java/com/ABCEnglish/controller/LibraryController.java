package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.LibraryItemRequest;
import com.ABCEnglish.dto.response.LibraryItemResponse;
import com.ABCEnglish.entity.Library;
import com.ABCEnglish.repository.LibraryRepository;
import com.ABCEnglish.service.LibraryItemService;
import com.ABCEnglish.service.LibraryService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryItemService libraryItemService;

    @PostMapping("/add")
    public Library addLibrary(@RequestBody String libraryName,
                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryService.addLibrary(libraryName,token);
    }
    @PostMapping("/add-item")
    public LibraryItemResponse addLibraryItem(@RequestBody LibraryItemRequest libraryItemRequest,
                                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader ) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.addItemToLibrary(libraryItemRequest,token);
    }
    @GetMapping("/get-all/{libraryId}")
    public Page<LibraryItemResponse> getAllLibraryItems(
            @PathVariable Integer libraryId,
            Pageable pageable,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryItemService.getAllItems(libraryId,pageable,token);
    }
}
