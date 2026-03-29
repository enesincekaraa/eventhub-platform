package com.enesincekara.eventhub.user.api.dto;

public record RegisterUserRequest(
        String email,
        String password
) {}