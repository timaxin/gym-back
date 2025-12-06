package com.learning.gymback.mapper;

import com.learning.gymback.dto.UserAdminResponseDto;
import com.learning.gymback.security.entity.SecurityUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserForAdminMapper {

    UserAdminResponseDto toUserAdminResponseDto(SecurityUser securityUser);

}
