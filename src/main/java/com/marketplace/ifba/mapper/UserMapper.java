package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toEntity(UserRequest request);

    public UserResponse toDTO(User user);
}
