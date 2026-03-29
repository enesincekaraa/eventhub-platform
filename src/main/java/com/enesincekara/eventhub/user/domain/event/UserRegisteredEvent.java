package com.enesincekara.eventhub.user.domain.event;

import java.util.UUID;

public class UserRegisteredEvent {
    private final UUID userId;
    private final String email;
    public UserRegisteredEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
}
