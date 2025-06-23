package com.ABCEnglish.repository;

import com.ABCEnglish.entity.LibraryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem,Integer> {

    List<LibraryItem> findByLibraryItemId(Integer libraryId);

    Page<LibraryItem> findByLibrary_LibraryId(Integer libraryId, Pageable  pageable);
}
