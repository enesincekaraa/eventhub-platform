package com.enesincekara.eventhub.user.application;

import com.enesincekara.eventhub.user.domain.User;
import com.enesincekara.eventhub.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;

    @Transactional
    public User registerUser(String email,String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email zaten kayıtlı");
        }
        User user = User.create(email, password);
        return userRepository.save(user);
    }
}
