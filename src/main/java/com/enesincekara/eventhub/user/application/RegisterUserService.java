package com.enesincekara.eventhub.user.application;

import com.enesincekara.eventhub.common.error.AlreadyExistsException;
import com.enesincekara.eventhub.common.outbox.domain.OutboxEvent;
import com.enesincekara.eventhub.common.outbox.infrastructure.OutboxRepository;
import com.enesincekara.eventhub.user.domain.User;
import com.enesincekara.eventhub.user.domain.event.UserRegisteredEvent;
import com.enesincekara.eventhub.user.infrastructure.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    @Transactional
    public User registerUser(String email,String password){
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException();
        }
        User user = User.create(email, password);
        User saved = userRepository.save(user);
        saveUserRegisteredOutboxEvent(saved);
        log.info("User {} registered successfully", email);
        return saved;
    }

    private void saveUserRegisteredOutboxEvent(User user){
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(),user.getEmail()
        );
        OutboxEvent outboxEvent = new OutboxEvent(
                "User",
                user.getId().toString(),
                event.getClass().getSimpleName(),
                toJson(event)
        );
        outboxRepository.save(outboxEvent);
    }


    private String toJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);

        }catch (JsonProcessingException e){
            log.error("Json serialization error for object: {} ",object,e);
            throw new RuntimeException("Json serialization error for object: "+object,e);
        }
    }
}
