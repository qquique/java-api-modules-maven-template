package com.qquique.jamm.application.mapper;

import com.qquique.jamm.application.dto.UserDTO;
import com.qquique.jamm.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO mapToDTO(User user);

    User mapToDomain(UserDTO userDTO);

    void updateUserFromDTo(UserDTO userDto, @MappingTarget User user);
}
