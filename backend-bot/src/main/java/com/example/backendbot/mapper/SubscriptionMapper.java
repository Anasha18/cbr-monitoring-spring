package com.example.backendbot.mapper;

import com.example.backendbot.controller.dto.subscription.SubscriptionRequestDto;
import com.example.backendbot.controller.dto.subscription.SubscriptionResponseDto;
import com.example.backendbot.domain.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "currency.id", source = "currencyId")
    Subscription toDomain(SubscriptionRequestDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "currencyId", source = "currency.id")
    SubscriptionResponseDto toDto(Subscription subscription);

    List<SubscriptionResponseDto> toDto(List<Subscription> subscriptions);
}
