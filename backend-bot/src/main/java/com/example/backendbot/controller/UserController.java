package com.example.backendbot.controller;

import com.example.backendbot.controller.dto.user.UserRequestDto;
import com.example.backendbot.controller.dto.user.UserResponseDto;
import com.example.backendbot.domain.User;
import com.example.backendbot.mapper.UserMapper;
import com.example.backendbot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        User user = userService.findById(id);

        return userMapper.toDto(user);
    }

    @PostMapping("/get-or-create")
    public UserResponseDto getOrCreate(
            @Valid @RequestBody UserRequestDto dto
    ) {
        User user = userService.getOrCreateUserByTelegramId(dto);

        return userMapper.toDto(user);
    }
}
