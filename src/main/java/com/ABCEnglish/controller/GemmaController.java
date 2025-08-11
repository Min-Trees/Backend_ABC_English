package com.ABCEnglish.controller;
import com.ABCEnglish.service.GemmaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/writing")
public class GemmaController {
    private final GemmaService gemmaService;

    public GemmaController(GemmaService gemmaService) {
        this.gemmaService = gemmaService;
    }

    @PostMapping("/evaluate")
    public String evaluateWriting(@RequestBody String essay) {
        return gemmaService.evaluateWriting(essay);
    }
}