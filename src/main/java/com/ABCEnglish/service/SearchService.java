package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.SearchRequest;
import com.ABCEnglish.dto.response.SearchResult;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.repository.CourseRepository;
import com.ABCEnglish.repository.DocRepository;
import com.ABCEnglish.repository.ExerciseRepository;
import com.ABCEnglish.repository.LessonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String type = searchRequest.getType();
        String keyword = searchRequest.getKeyword() == null ? "" : searchRequest.getKeyword();

        if (type == null || type.equalsIgnoreCase("course")) {
            Specification<Course> specification = buildCourseSpecification(keyword, searchRequest);

            return courseRepository.findAll(specification, pageable)
                    .map(course -> new SearchResult(
                            course.getName(),
                            course.getFee(),
                            course.getType().name(),
                            course.getDescription(),
                            course.getCreator() != null ? course.getCreator().getFullname() : null,
                            course.getCreatedAt()
                    ));

        } else if (type.equalsIgnoreCase("lesson")) {
            Specification<Lesson> specification = buildLessonSpecification(keyword, searchRequest);

            return lessonRepository.findAll(specification, pageable)
                    .map(lesson -> new SearchResult(
                            lesson.getName(),
                            null,
                            "LESSON",
                            lesson.getContent(),
                            lesson.getCreator() != null ? lesson.getCreator().getFullname() : null,
                            lesson.getCreatedAt()
                    ));

        } else if (type.equalsIgnoreCase("doc")) {
            Specification<Doc> specification = buildDocSpecification(keyword, searchRequest);

            return docRepository.findAll(specification, pageable)
                    .map(doc -> new SearchResult(
                            doc.getName(),
                            null,
                            "DOC",
                            doc.getDescription(),
                            doc.getCreator() != null ? doc.getCreator().getFullname() : null,
                            doc.getCreatedAt()
                    ));

        } else if (type.equalsIgnoreCase("exercise")) {
            Specification<Exercises> specification = buildExerciseSpecification(keyword, searchRequest);

            return exerciseRepository.findAll(specification, pageable)
                    .map(exercise -> new SearchResult(
                            exercise.getTitle(),
                            null,
                            "EXERCISE",
                            exercise.getContent(),
                            exercise.getCreator() != null ? exercise.getCreator().getFullname() : null,
                            exercise.getCreatedAt()
                    ));

        } else if (type.equalsIgnoreCase("all")) {
            List<SearchResult> results = new ArrayList<>();

            // Courses
            results.addAll(courseRepository.findAll(buildCourseSpecification(keyword, searchRequest), pageable)
                    .map(course -> new SearchResult(
                            course.getName(),
                            course.getFee(),
                            course.getType().name(),
                            course.getDescription(),
                            course.getCreator() != null ? course.getCreator().getFullname() : null,
                            course.getCreatedAt()
                    )).getContent());

            // Lessons
            results.addAll(lessonRepository.findAll(buildLessonSpecification(keyword, searchRequest), pageable)
                    .map(lesson -> new SearchResult(
                            lesson.getName(),
                            null,
                            "LESSON",
                            lesson.getContent(),
                            lesson.getCreator() != null ? lesson.getCreator().getFullname() : null,
                            lesson.getCreatedAt()
                    )).getContent());

            // Docs
            results.addAll(docRepository.findAll(buildDocSpecification(keyword, searchRequest), pageable)
                    .map(doc -> new SearchResult(
                            doc.getName(),
                            null,
                            "DOC",
                            doc.getDescription(),
                            doc.getCreator() != null ? doc.getCreator().getFullname() : null,
                            doc.getCreatedAt()
                    )).getContent());

            // Exercises
            results.addAll(exerciseRepository.findAll(buildExerciseSpecification(keyword, searchRequest), pageable)
                    .map(exercise -> new SearchResult(
                            exercise.getTitle(),
                            null,
                            "EXERCISE",
                            exercise.getContent(),
                            exercise.getCreator() != null ? exercise.getCreator().getFullname() : null,
                            exercise.getCreatedAt()
                    )).getContent());

            return new PageImpl<>(results, pageable, results.size());
        }

        return Page.empty();
    }

    private Specification<Course> buildCourseSpecification(String keyword, SearchRequest request) {
        Specification<Course> specification = Specification.where(null);

        if (!keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + keyword + "%")
            );
        }

        if (request.getMinPrice() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("fee"), request.getMinPrice())
            );
        }

        if (request.getMaxPrice() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("fee"), request.getMaxPrice())
            );
        }

        if (request.getStartDate() != null) {
            Date start = Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            );
        }

        if (request.getEndDate() != null) {
            Date end = Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), end)
            );
        }

        return specification;
    }

    private Specification<Lesson> buildLessonSpecification(String keyword, SearchRequest request) {
        Specification<Lesson> specification = Specification.where(null);

        if (!keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + keyword + "%")
            );
        }

        if (request.getStartDate() != null) {
            Date start = Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            );
        }

        if (request.getEndDate() != null) {
            Date end = Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), end)
            );
        }

        return specification;
    }

    private Specification<Doc> buildDocSpecification(String keyword, SearchRequest request) {
        Specification<Doc> specification = Specification.where(null);

        if (!keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + keyword + "%")
            );
        }

        if (request.getStartDate() != null) {
            Date start = Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            );
        }

        if (request.getEndDate() != null) {
            Date end = Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), end)
            );
        }

        return specification;
    }

    private Specification<Exercises> buildExerciseSpecification(String keyword, SearchRequest request) {
        Specification<Exercises> specification = Specification.where(null);

        if (!keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), "%" + keyword + "%")
            );
        }

        if (request.getStartDate() != null) {
            Date start = Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            );
        }

        if (request.getEndDate() != null) {
            Date end = Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), end)
            );
        }

        return specification;
    }
}

