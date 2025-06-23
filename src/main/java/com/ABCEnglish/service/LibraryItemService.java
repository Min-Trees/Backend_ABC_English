package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.LibraryItemRequest;
import com.ABCEnglish.dto.response.LibraryItemResponse;
import com.ABCEnglish.entity.Library;
import com.ABCEnglish.entity.LibraryItem;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.LibraryItemMapper;
import com.ABCEnglish.repository.DocRepository;
import com.ABCEnglish.repository.LibraryItemRepository;
import com.ABCEnglish.repository.LibraryRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LibraryItemService {
    private final TokenService tokenService;
    private final LibraryItemRepository libraryItemRepository;
    private final QuestionRepository questionRepository;
    private final DocRepository docRepository;
    private final LibraryItemMapper libraryItemMapper;
    private final LibraryRepository libraryRepository;

    public LibraryItemResponse addItemToLibrary(LibraryItemRequest request, String token) throws ParseException, JOSEException {
        if (tokenService != null) {
            User user = tokenService.getAdmin(token,true);
        }
        if (libraryRepository != null) {
            Library library = libraryRepository.findById(request.getLibraryId())
                    .orElseThrow(() -> new AppException(ErrorCode.LIBRARY_NOT_FOUND));
        }
        boolean itemExists = switch (request.getType()) {
            case QUESTION -> questionRepository.existsById(request.getItemId());
            case DOCUMENT -> docRepository.existsById(request.getItemId());
        };
        LibraryItem libraryItem = libraryItemMapper.toLibraryItemEntity(request);
        libraryItem.setCreateAt(new Date());
        libraryItem.setUpdateAt(new Date());
        LibraryItem saved = libraryItemRepository.save(libraryItem);

        return libraryItemMapper.toResponse(saved);

    }
    public Page<LibraryItemResponse> getAllItems(Integer libraryId, Pageable pageable, String token) throws ParseException, JOSEException {
        if (tokenService != null) {
            User user = tokenService.getAdmin(token,true);
        }
        Page<LibraryItem> libraryItemList = libraryItemRepository.findByLibrary_LibraryId(libraryId,pageable);
        return libraryItemList.map(libraryItemMapper::toResponse);

    }


}
