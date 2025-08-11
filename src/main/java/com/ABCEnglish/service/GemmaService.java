package com.ABCEnglish.service;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GemmaService {
    private static final String API_KEY = "AIzaSyBmQtjnMnQHzjalbE9WP5iZsiFgM4OZhoM";  // Thay bằng API key từ Google AI Studio
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-3-27b-it:generateContent?key=" + API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public String evaluateWriting(String essay) {
        String prompt = "You're an English writing examiner. Please evaluate the following essay and return:\n" +
                "- Score (0-9)\n" +
                "- Feedback on grammar, vocabulary, coherence, task response\n" +
                "- Suggestions for improvement\n\nEssay:\n" + essay;

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            var candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                var contentMap = (Map<String, Object>) candidates.get(0).get("content");
                var parts = (List<Map<String, String>>) contentMap.get("parts");
                return parts.get(0).get("text");
            }
        }

        return "Evaluation failed. Please try again later.";
    }
}
