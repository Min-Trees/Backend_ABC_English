package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllBy(Pageable pageable);
    @Query("SELECT c FROM Comment c WHERE c.commentId = :commentId AND c.user.userId = :userId")
    Optional<Comment> findByCommentIdAndUserId(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

}
