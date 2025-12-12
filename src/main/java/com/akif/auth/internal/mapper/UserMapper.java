package com.akif.auth.internal.mapper;

import com.akif.auth.api.UserDto;
import com.akif.auth.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "active", source = "enabled")
    UserDto toDto(User user);
}
