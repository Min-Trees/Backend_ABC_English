package com.ABCEnglish.controller;

import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.ExerciseBlockQuestionResponse;
import com.ABCEnglish.dto.response.ExerciseBlockResponse;
import com.ABCEnglish.dto.response.ExerciseDeleteResponse;
import com.ABCEnglish.dto.response.ExerciseResponse;
import com.ABCEnglish.entity.ExerciseBlockQuestion;
import com.ABCEnglish.service.ExerciseBlockService;
import com.ABCEnglish.service.ExerciseService;
import com.ABCEnglish.service.TokenService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {
    ExerciseService exerciseService;
    TokenService tokenService;
    JwtUtil jwtUtil;
    private final ExerciseBlockService exerciseBlockService;

    @PostMapping("/{lessonId}")
    ApiResponse<ExerciseResponse> addExercise(
            @PathVariable Integer lessonId,
            @RequestBody ExerciseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
            ) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        ExerciseResponse result = exerciseService.createUser(lessonId,request,token);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @PutMapping("/{lessonId}/{exerciseId}")
    ApiResponse<ExerciseResponse> updateExercise(
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId,
            @RequestBody ExerciseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) throws ParseException, JOSEException{
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        ExerciseResponse result = exerciseService.updateExercise(lessonId,exerciseId,request,token);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Page<ExerciseResponse>> getAllExercises(Pageable pageable, @PathVariable Integer lessonId){
        Page<ExerciseResponse> result = exerciseService.getAllExercise(pageable, lessonId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{lessonId}/{exerciseId}")
    ApiResponse<ExerciseResponse> getExercise(
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) throws ParseException, JOSEException{
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        ExerciseResponse result = exerciseService.getExercise(lessonId,exerciseId,token);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @DeleteMapping("/{lessonId}/{exerciseId}")
    public ExerciseDeleteResponse deleteExercise(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;

        return exerciseService.deleteExercise(lessonId,exerciseId,token);
    }

    @PostMapping("/create-block/{exerciseId}")
    public ExerciseBlockResponse addBlock(
            @RequestBody ExerciseBlockRequest request,
            @PathVariable Integer exerciseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.createBlock(exerciseId,request,token);
    }
    @GetMapping("get-blocks/{exerciseId}")
    public List<ExerciseBlockResponse> getBlocksByExerciseByOrderIndexAsc(
            @PathVariable Integer exerciseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.getAllByOrderByOrderIndexAsc(exerciseId,token);
    }

    @DeleteMapping("/delete/{exerciseId}/{blockId}")
    public String deleteBlock(
            @PathVariable Integer exerciseId,
            @PathVariable Integer blockId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.deleteBlock(exerciseId,blockId,token);
    }

    @DeleteMapping("delete-question/{exerciseBlockId}/{exerciseBlockQuestionId}")
    public String deleteBlockQuestion(
            @PathVariable Integer exerciseBlockId,
            @PathVariable Integer exerciseBlockQuestionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.deleteQuestionFromBlock(exerciseBlockId,exerciseBlockQuestionId,token);
    }

    @PostMapping("/create-block-question")
    public String addBlockQuestion(
            @RequestBody ExerciseBlockQuestionRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.addQuestionToBlock(request,token);
    }

    @GetMapping("/by-block/{exerciseBlockId}")
    public List<ExerciseBlockQuestionResponse> getQuestionsByBlockId(
            @PathVariable Integer exerciseBlockId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        return exerciseBlockService.getQuestionsById(exerciseBlockId,token);
    }


}
