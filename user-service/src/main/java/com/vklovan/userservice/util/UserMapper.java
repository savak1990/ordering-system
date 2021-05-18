package com.vklovan.userservice.util;

import com.vklovan.userservice.dto.UserDto;
import com.vklovan.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
