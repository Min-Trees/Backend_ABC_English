package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.SocialRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.dto.response.SocialDeleteResponse;
import com.ABCEnglish.dto.response.SocialResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Social;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.SocialMapper;
import com.ABCEnglish.reponsesitory.SocialRepository;
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
public class SocialService {
    UserRepository userRepository;
    AuthenticationService authenticationService;
    SocialMapper socialMapper;
    SocialRepository socialRepository;
    public SocialResponse createPost(SocialRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Social social = socialMapper.toSocial(request);
        social.setUser(user);
        social.setCreatedAt(new Date());
        social.setUpdatedAt(new Date());
        socialRepository.save(social);
        return socialMapper.socialResponse(social);
    }

    public SocialResponse updatePost(Integer socialId, SocialRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException(ErrorCode.SOCIAL_NOT_FOUND));
        socialMapper.updateSocial(request,social);
        social.setUpdatedAt(new Date());
        Social updateSocial = socialRepository.save(social);
        return socialMapper.socialResponse(updateSocial);
    }
     public SocialResponse getSocial(Integer socialId){
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException(ErrorCode.SOCIAL_NOT_FOUND));
        return socialMapper.socialResponse(social);
     }

    public Page<SocialResponse> getAllSocial(Pageable pageable){
        // thuc hien phan trang
        Page<Social> socials = socialRepository.findAllBy(pageable);
        return socials.map(socialMapper::socialResponse);
    }
// can fix delete phai la user tao post
    public SocialDeleteResponse deleteSocial(Integer socialId, IntrospectRequest token){
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new AppException(ErrorCode.SOCIAL_NOT_FOUND));
        socialRepository.delete(social);
        SocialDeleteResponse socialDeleteResponse = new SocialDeleteResponse();
        socialDeleteResponse.setSocialId(socialId);
        socialDeleteResponse.setStatus(false);
        socialDeleteResponse.setMessage("delete success");
        return ApiResponse.<SocialDeleteResponse>builder().result(socialDeleteResponse).build().getResult();
    }
}
