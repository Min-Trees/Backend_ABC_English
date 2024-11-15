package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.CommentRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CommentDeleteResponse;
import com.ABCEnglish.dto.response.CommentResponse;
import com.ABCEnglish.dto.response.DocResponse;
import com.ABCEnglish.entity.Comment;
import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.Social;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.CommentMapper;
import com.ABCEnglish.reponsesitory.CommentRepository;
import com.ABCEnglish.reponsesitory.SocialRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SocialRepository socialRepository;
    CommentMapper commentMapper;
    @Autowired
    CommentRepository commentRepository;
    public CommentResponse createComment(CommentRequest request, Integer socialId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException((ErrorCode.SOCIAL_NOT_FOUND)));
        Comment comment = commentMapper.toComment(request);
        comment.setUser(user);
        comment.setSocial(social);

        commentRepository.save(comment);
        return commentMapper.commentResponse(comment);
    }
    public CommentResponse updateComment(CommentRequest request, Integer socialId,Integer commentId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException((ErrorCode.SOCIAL_NOT_FOUND)));
        Comment comment = commentRepository.findByCommentIdAndUserId(commentId,userId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentMapper.updateComment(request,comment);
        Comment updateComment = commentRepository.save(comment);
        return commentMapper.commentResponse(updateComment);
    }

    public Page<CommentResponse> getAllComment(Pageable pageable){
        // thuc hien phan trang
        Page<Comment> comments = commentRepository.findAllBy(pageable);
        return comments.map(commentMapper::commentResponse);
    }

    public CommentResponse getComment(Integer socialId,Integer commentId){
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException(ErrorCode.SOCIAL_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        return commentMapper.commentResponse(comment);
    }

    public CommentDeleteResponse deleteResponse(Integer socialId, Integer commentId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException((ErrorCode.SOCIAL_NOT_FOUND)));
        Comment comment = commentRepository.findByCommentIdAndUserId(commentId,userId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
        CommentDeleteResponse commentDeleteResponse = new CommentDeleteResponse();
        commentDeleteResponse.setCommentId(commentId);
        commentDeleteResponse.setStatus(false);
        commentDeleteResponse.setMessage("delete success");
        return commentDeleteResponse;
    }
}
