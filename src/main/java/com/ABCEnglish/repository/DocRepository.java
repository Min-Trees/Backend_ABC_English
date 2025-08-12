package com.ABCEnglish.repository;

import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.DocTopic;
import com.ABCEnglish.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocRepository extends JpaRepository<Doc, Integer>, JpaSpecificationExecutor<Doc> {
    Page<Doc> findAllBy(Pageable pageable);
    Page<Doc> findByLesson(Pageable pageable, Lesson lesson);
    @Query("SELECT d FROM Doc d WHERE d.lesson = :lesson AND d.status = true")
    Page<Doc> findAllByLesson(Lesson lesson, Pageable pageable);
    List<Doc> findAllByLesson(Lesson lesson);
    Page<Doc> findByTopic(DocTopic topic, Pageable pageable);
    Page<Doc> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
