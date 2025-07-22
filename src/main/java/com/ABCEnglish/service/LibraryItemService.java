package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.LibraryDocRequest;
import com.ABCEnglish.dto.request.LibraryQuestionRequest;
import com.ABCEnglish.dto.response.LibraryDocResponse;
import com.ABCEnglish.dto.response.LibraryQuestionDeleteResponse;
import com.ABCEnglish.dto.response.LibraryQuestionResponse;
import com.ABCEnglish.entity.LibraryDoc;
import com.ABCEnglish.entity.LibraryQuestion;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.LibraryDocMapper;
import com.ABCEnglish.mapper.LibraryQuestionMapper;
import com.ABCEnglish.repository.*;
import lombok.AllArgsConstructor;


import net.loomchild.segment.util.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class LibraryItemService {
    private final TokenService tokenService;
    private final LibraryQuestionRepository libraryQuestionRepository;
    private final LibraryQuestionMapper libraryQuestionMapper;
    private final LibraryDocMapper libraryDocMapper;
    private final LibraryDocRepository libraryDocRepository;

    public LibraryQuestionResponse addQuestionToLibrary(LibraryQuestionRequest libraryQuestionRequest, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        LibraryQuestion libraryQuestion = libraryQuestionMapper.toEntity(libraryQuestionRequest);
        libraryQuestion.setCreatedAt(new Date());
        libraryQuestion.setUpdatedAt(new Date());
        LibraryQuestion savedQuestion = libraryQuestionRepository.save(libraryQuestion);
        return libraryQuestionMapper.toResponse(savedQuestion);
    }

    public LibraryQuestionResponse updateQuestionToLibrary(Integer libraryQuestionId,LibraryQuestionRequest libraryQuestionRequest, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        LibraryQuestion libraryQuestion = libraryQuestionRepository.findById(libraryQuestionId)
                .orElseThrow(()-> new ResourceNotFoundException("Library Question Not Found"));
        libraryQuestion.setUpdatedAt(new Date());
        LibraryQuestion updatedQuestion = libraryQuestionMapper.updateFromRequest(libraryQuestionRequest,libraryQuestion);
        LibraryQuestion savedQuestion = libraryQuestionRepository.save(updatedQuestion);
        return libraryQuestionMapper.toResponse(savedQuestion);
    }

    public LibraryQuestionResponse getQuestionFromLibrary(Integer libraryQuestionId, String token) {
        if(tokenService != null){
            User user = tokenService.getUserFromToken(token);
        }
        return libraryQuestionMapper.toResponse(libraryQuestionRepository.findById(libraryQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Library Question Not Found")));

    }
    public Page<LibraryQuestionResponse> getQuestionsFromLibrary(Integer libraryId, Pageable pageable, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        Page<LibraryQuestion> libraryQuestions = libraryQuestionRepository.findByLibrary_LibraryId(libraryId, pageable);
        return libraryQuestions.map(libraryQuestionMapper::toResponse);
    }

    public LibraryQuestionDeleteResponse deleteQuestionFromLibrary(Integer libraryQuestionId, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        LibraryQuestion libraryQuestion = libraryQuestionRepository.findById(libraryQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Library Question Not Found"));
        LibraryQuestionDeleteResponse response = new LibraryQuestionDeleteResponse();
        response.setLibraryId(libraryQuestion.getLibraryItemId());
        response.setStatus(true);
        response.setMessage("Success");
        return response;
    }


    public LibraryDocResponse addDocToLibrary(LibraryDocRequest libraryDocRequest, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);

        }
        LibraryDoc libraryDoc = libraryDocMapper.toEntity(libraryDocRequest);
        libraryDoc.setCreatedAt(new Date());
        libraryDoc.setUpdatedAt(new Date());
        LibraryDoc saveDoc = libraryDocRepository.save(libraryDoc);
        return libraryDocMapper.toResponse(saveDoc);
    }

    public LibraryDocResponse getDoc(Integer libraryDocId, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }

        LibraryDoc libraryDoc = libraryDocRepository.findById(libraryDocId)
                .orElseThrow(() -> new AppException(ErrorCode.LIBRARY_ITEM_NOT_FOUND));
        return libraryDocMapper.toResponse(libraryDoc);
    }

    public LibraryDocResponse updateDoc(Integer docId, LibraryDocRequest libraryDocRequest, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        LibraryDoc libraryDoc = libraryDocRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.LIBRARY_ITEM_NOT_FOUND));
        libraryDoc.setUpdatedAt(new Date());
        libraryDocMapper.updateFromRequest(libraryDocRequest,libraryDoc);
        LibraryDoc savedDoc = libraryDocRepository.save(libraryDoc);
        return libraryDocMapper.toResponse(savedDoc);
    }

    public LibraryDocResponse deleteDoc(Integer docId, String token) {
        if(tokenService != null){
            User user = tokenService.getAdmin(token,true);
        }
        LibraryDoc libraryDoc = libraryDocRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.LIBRARY_ITEM_NOT_FOUND));
        libraryDoc.setDeleted(true);
        return libraryDocMapper.toResponse(libraryDocRepository.save(libraryDoc));
    }

}
