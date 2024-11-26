package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.CourseOfUser;
import com.ABCEnglish.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseOfUserRepository extends JpaRepository<CourseOfUser, Integer> {
    /**
     * Tìm tất cả các bản ghi CourseOfUser theo User với phân trang.
     *
     * @param user     Người dùng cần tìm khóa học.
     * @param pageable Cấu hình phân trang.
     * @return Danh sách CourseOfUser dạng phân trang.
     */
    Page<CourseOfUser> findAllByUser(User user, Pageable pageable);
}