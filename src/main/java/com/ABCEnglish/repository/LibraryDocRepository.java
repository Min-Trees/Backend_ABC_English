package com.ABCEnglish.repository;

import com.ABCEnglish.entity.LibraryDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryDocRepository extends JpaRepository<LibraryDoc,Integer> {
    @EntityGraph(attributePaths = {"doc"})
    Page<LibraryDoc> findByLibrary_LibraryId(Integer libraryId, Pageable pageable);
}
