package com.enesincekara.eventhub.notification.domain;

public class Notification {
    private final String email;
    private final String message;
    public Notification(String email, String message) {
        this.email = email;
        this.message = message;
    }
    public String getEmail() {
        return email;
    }
    public String getMessage() {
        return message;
    }
}
