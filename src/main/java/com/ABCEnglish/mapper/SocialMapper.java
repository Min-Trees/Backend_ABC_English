package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.SocialRequest;
import com.ABCEnglish.dto.response.SocialResponse;
import com.ABCEnglish.entity.Social;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface SocialMapper {
    Social toSocial(SocialRequest request);
    @Mapping(source = "user.userId", target = "user")
    SocialResponse socialResponse(Social social);
}
