package com.example.backendbot.mapper;

import com.example.backendbot.controller.dto.subscription.SubscriptionRequestDto;
import com.example.backendbot.controller.dto.subscription.SubscriptionResponseDto;
import com.example.backendbot.domain.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toDomain(SubscriptionRequestDto dto);

    SubscriptionResponseDto toDto(Subscription subscription);

    List<SubscriptionResponseDto> toDto(List<Subscription> subscriptions);
}
