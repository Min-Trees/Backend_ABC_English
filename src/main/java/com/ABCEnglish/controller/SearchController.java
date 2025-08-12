package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.SearchRequest;
import com.ABCEnglish.dto.response.SearchResult;
import com.ABCEnglish.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {

    SearchService searchService;

    @GetMapping
    public ResponseEntity<Page<SearchResult>> search(SearchRequest request, Pageable pageable) {
        Page<SearchResult> results = searchService.search(request, pageable);
        return ResponseEntity.ok(results);
    }
}

