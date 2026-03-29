package com.enesincekara.eventhub.user.application;

import com.enesincekara.eventhub.common.kafka.KafkaProducer;
import com.enesincekara.eventhub.user.domain.User;
import com.enesincekara.eventhub.user.domain.event.UserRegisteredEvent;
import com.enesincekara.eventhub.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public User registerUser(String email,String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email zaten kayıtlı");
        }
        User user = User.create(email, password);
        User saved = userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                saved.getId(),
                saved.getEmail()
        );
        kafkaProducer.sendMessage("user.registered", event);
        log.info("event {} published", saved.getId());
        return saved;
    }
}
