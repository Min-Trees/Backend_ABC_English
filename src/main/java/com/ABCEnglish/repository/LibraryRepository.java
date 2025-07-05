package com.ABCEnglish.repository;

import com.ABCEnglish.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library,Integer> {
}
