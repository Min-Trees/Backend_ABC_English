package com.ABCEnglish.controller;

import com.ABCEnglish.entity.Library;
import com.ABCEnglish.service.LibraryItemService;
import com.ABCEnglish.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/add")
    public Library addLibrary(@RequestBody String libraryName,
                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return libraryService.addLibrary(libraryName,token);
    }




}
