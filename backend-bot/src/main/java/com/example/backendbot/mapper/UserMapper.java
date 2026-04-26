package com.example.backendbot.mapper;

import com.example.backendbot.controller.dto.user.UserRequestDto;
import com.example.backendbot.controller.dto.user.UserResponseDto;
import com.example.backendbot.domain.User;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
