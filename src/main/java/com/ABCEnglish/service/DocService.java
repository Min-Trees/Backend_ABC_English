package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.DocRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.dto.response.DocDeleteResponse;
import com.ABCEnglish.dto.response.DocResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.DocMapper;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.DocRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocService {
    DocRepository docRepository;
    UserRepository userRepository;
    AuthenticationService authenticationService;
    CourseRepository courseRepository;
    DocMapper docMapper;

    public DocResponse createDoc(Integer courseId,DocRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        // lay thong tin user tu token
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));


        Doc doc = docMapper.toDoc(request);

        // luu tru nguoi tao tai lieu
        doc.setCreator(user);
        doc.setCourse(course);
        doc.setCreatedAt(new Date());
        doc.setUpdatedAt(new Date());
        docRepository.save(doc);
        return docMapper.docResponse(doc);
    }

    public DocResponse updateDoc(Integer courseId, Integer docId, DocRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));

        docMapper.updateDoc(request,doc);
        doc.setUpdatedAt(new Date());
        Doc updateDoc = docRepository.save(doc);
        return docMapper.docResponse(updateDoc);
    }


    public Page<DocResponse> getAllDoc(Pageable pageable){
        // thuc hien phan trang
        Page<Doc> docs = docRepository.findAllBy(pageable);
        return docs.map(docMapper::docResponse);
    }

    public DocResponse getDoc(Integer courseId, Integer docId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));
        return docMapper.docResponse(doc);
    }

    public DocDeleteResponse deleteDoc(Integer courseId,Integer docId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));

        docRepository.delete(doc);
        DocDeleteResponse docDeleteResponse = new DocDeleteResponse();
        docDeleteResponse.setDocId(docId);
        docDeleteResponse.setStatus(false);
        docDeleteResponse.setMessage("delete success");
        return ApiResponse.<DocDeleteResponse>builder().result(docDeleteResponse).build().getResult();
    }

}
