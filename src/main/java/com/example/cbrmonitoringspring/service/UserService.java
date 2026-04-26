package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.integration.UserFeignClient;
import com.example.cbrmonitoringspring.integration.dto.user.UserRequestDto;
import com.example.cbrmonitoringspring.integration.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFeignClient userFeignClient;

    public UserResponseDto findById(Long id) {
        return userFeignClient.findById(id);
    }

    public UserResponseDto getOrCreateUserByTelegramId(String username, Long telegramId) {
        return userFeignClient.getOrCreate(
                new UserRequestDto(username, telegramId)
        );
    }
}
