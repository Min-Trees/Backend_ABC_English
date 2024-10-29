package com.ABCEnglish.mapper;
import com.ABCEnglish.dto.request.SocialRequest;
import com.ABCEnglish.dto.response.SocialResponse;
import com.ABCEnglish.entity.Social;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface SocialMapper {
    Social toSocial(SocialRequest request);
    @Mapping(source = "user.userId", target = "userId")  // Map userId chứ không phải User
    SocialResponse socialResponse(Social social);

    void updateSocial(SocialRequest request, @MappingTarget Social social);

}
