package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.SearchRequest;
import com.ABCEnglish.dto.response.SearchResult;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.DocRepository;
import com.ABCEnglish.reponsesitory.ExerciseRepository;
import com.ABCEnglish.reponsesitory.LessonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
    CourseRepository courseRepository;
    DocRepository docRepository;
    LessonRepository lessonRepository;
    ExerciseRepository exerciseRepository;

    public Page<SearchResult> search(SearchRequest searchRequest, Pageable pageable){
        Specification<Course> specification = Specification.where(null);

        if (searchRequest.getKeyword() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + searchRequest.getKeyword() + "%")
            );
        }

        if (searchRequest.getCourseType() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), searchRequest.getCourseType())
            );
        }

        if (searchRequest.getMinPrice() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("fee"), searchRequest.getMinPrice())
            );
        }

        if (searchRequest.getMaxPrice() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("fee"), searchRequest.getMaxPrice())
            );
        }

        return courseRepository.findAll(specification, pageable)
                .map(course -> new SearchResult(
                        course.getName(),
                        course.getFee(),
                        course.getType().name(),      // Loại dữ liệu, ví dụ "IELTS", "TOEIC"
                        course.getDescription(),      // Mô tả khóa học
                        course.getCreator().getFullname(), // Tên người tạo khóa học
                        course.getCreatedAt()          // Ngày tạo khóa học
                ));
    }
}
