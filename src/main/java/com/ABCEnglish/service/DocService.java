package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.DocRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.*;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.DocMapper;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.DocRepository;
import com.ABCEnglish.reponsesitory.LessonRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocService {
    DocRepository docRepository;
    UserRepository userRepository;
    AuthenticationService authenticationService;
    LessonRepository lessonRepository;
    DocMapper docMapper;
    UserService userService;
    public DocResponse createDoc(Integer lessonId,DocRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        // lay thong tin user tu token
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.getRole().getRoleId()!=2 && user.getRole().getRoleId()!=3) {
            throw new AppException(ErrorCode.NOT_APPECT_ROLE);
        }
        boolean isAdmin = userService.isAdmin(userId);
        if (!isAdmin) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));


        Doc doc = docMapper.toDoc(request);

        // luu tru nguoi tao tai lieu
        doc.setCreator(user);
        doc.setLesson(lesson);
        doc.setCreatedAt(new Date());
        doc.setUpdatedAt(new Date());
        docRepository.save(doc);
        return docMapper.docResponse(doc);
    }

    public DocResponse updateDoc(Integer lessonId, Integer docId, DocRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        if(user.getRole().getRoleId()!=2 && user.getRole().getRoleId()!=3){
            throw new AppException(ErrorCode.NOT_APPECT_ROLE);
        }
        boolean isAdmin = userService.isAdmin(userId);
        if (!isAdmin) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));

        docMapper.updateDoc(request,doc);
        doc.setUpdatedAt(new Date());
        Doc updateDoc = docRepository.save(doc);
        return docMapper.docResponse(updateDoc);
    }


    public Page<DocResponse> getAllDoc(Pageable pageable, Integer lessonId){
        // thuc hien phan trang
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        Page<Doc> docs = docRepository.findByLesson(pageable, lesson);
        return docs.map(docMapper::docResponse);
    }

    public DocResponse getDoc(Integer lessonId, Integer docId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));
        return docMapper.docResponse(doc);
    }
    public Page<DocResponse> getByLesson(Integer lessonId,Pageable pageable) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        Page<Doc> docs=docRepository.findAllByLesson(lesson,pageable);
        return docs.map(docMapper::docResponse);
    }


    public DocDeleteResponse deleteDoc(Integer lessonId,Integer docId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.getRole().getRoleId()!=2 && user.getRole().getRoleId()!=3) {
            throw new AppException(ErrorCode.NOT_APPECT_ROLE);
        }
        boolean isAdmin = userService.isAdmin(userId);
        if (!isAdmin) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Doc doc = docRepository.findById(docId)
                .orElseThrow(() -> new AppException(ErrorCode.DOC_NOT_FOUND));
        doc.setStatus(false);
        docRepository.save(doc);
        DocDeleteResponse docDeleteResponse = new DocDeleteResponse();
        docDeleteResponse.setDocId(docId);
        docDeleteResponse.setStatus(false);
        docDeleteResponse.setMessage("delete success");
        return ApiResponse.<DocDeleteResponse>builder().result(docDeleteResponse).build().getResult();
    }

    public void deleteDocsByLesson(Lesson lesson) {
        List<Doc> docs = docRepository.findAllByLesson(lesson);
        for(Doc doc: docs){
            doc.setStatus(false);
        }
        docRepository.saveAll(docs);
    }

}
