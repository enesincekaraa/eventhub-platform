package com.enesincekara.eventhub.user.domain;

import com.enesincekara.eventhub.common.error.DomainException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String email;
    private String password;

    protected User() {}

    private User(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static User create(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        return new User(UUID.randomUUID(), email, password);
    }

    private static void validateEmail(String email) {
        if (email ==  null || email.isBlank()) {
            throw new DomainException();
        }
        if (!email.contains("@")) {
            throw new DomainException();
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new DomainException();
        }
    }

    public UUID getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

}
