package com.example.backendbot.service;

import com.example.backendbot.controller.dto.user.UserRequestDto;
import com.example.backendbot.domain.User;
import com.example.backendbot.exception.NotFoundException;
import com.example.backendbot.mapper.UserMapper;
import com.example.backendbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Cacheable(cacheNames = "usersByTelegramId", key = "#telegramId")
    public Optional<User> findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @CachePut(cacheNames = "usersByTelegramId", key = "#result.telegramId")
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public User getOrCreateUserByTelegramId(UserRequestDto dto) {
        Long telegramId = dto.telegramId();
        String username = dto.username();

        Optional<User> foundedUser = findByTelegramId(telegramId);

        if (foundedUser.isPresent()) {
            return foundedUser.get();
        }
        User newUser = User.builder()
                .telegramId(telegramId)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();

        return save(newUser);
    }
}
