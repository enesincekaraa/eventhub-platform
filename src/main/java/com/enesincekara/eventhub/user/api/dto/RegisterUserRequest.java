package com.enesincekara.eventhub.user.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Email boş olamaz")
        @Email(message = "Geçersiz emial formatı")
        String email,

        @NotBlank(message = "Şifre boş olamaz")
        @Size(min = 4,message = "Şifre en az 4 karakter olmalı")
        String password
) {}