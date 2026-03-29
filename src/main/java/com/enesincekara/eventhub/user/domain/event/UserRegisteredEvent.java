package com.enesincekara.eventhub.user.domain.event;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class UserRegisteredEvent {

    private final UUID userId;
    private final String email;

    @JsonCreator
    public UserRegisteredEvent(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("email") String email
    ) {
        this.userId = userId;
        this.email = email;
    }

    public UUID getUserId() { return userId; }
    public String getEmail() { return email; }
}
