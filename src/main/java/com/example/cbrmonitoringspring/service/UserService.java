package com.example.cbrmonitoringspring.service;

import com.example.cbrmonitoringspring.domain.User;
import com.example.cbrmonitoringspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Cacheable(cacheNames = "usersByTelegramId", key = "#telegramId")
    public Optional<User> findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Transactional
    @CachePut(cacheNames = "usersByTelegramId", key = "#result.telegramId")
    public User save(User user) {
        return userRepository.save(user);
    }

    public User getOrCreateUserByTelegramId(String username, Long telegramId) {
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
