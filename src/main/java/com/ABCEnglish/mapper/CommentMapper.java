package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.CommentRequest;
import com.ABCEnglish.dto.response.CommentResponse;
import com.ABCEnglish.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest request);
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "social.socialId", target = "socialId")
    CommentResponse  commentResponse(Comment comment);

    void updateComment(CommentRequest request, @MappingTarget Comment comment);
}
