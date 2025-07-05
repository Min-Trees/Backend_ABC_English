package com.ABCEnglish.repository;

import com.ABCEnglish.entity.LibraryQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryQuestionRepository extends JpaRepository<LibraryQuestion,Integer> {
    @EntityGraph(attributePaths = {"question"})
    Page<LibraryQuestion> findByLibrary_LibraryId(Integer libraryId, Pageable pageable);

}
