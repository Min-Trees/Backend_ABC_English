package com.ABCEnglish.service;

import com.ABCEnglish.entity.Library;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.repository.LibraryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;
    private TokenService tokenService;
    public Library addLibrary(String name, String token) {
        if (tokenService != null) {
            User user = tokenService.getAdmin(token,true);
        }
        Library library = new Library();
        library.setLibraryName(name);
        return libraryRepository.save(library);
    }
}
