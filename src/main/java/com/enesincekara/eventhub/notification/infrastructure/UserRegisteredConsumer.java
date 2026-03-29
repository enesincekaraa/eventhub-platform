package com.enesincekara.eventhub.notification.infrastructure;

import com.enesincekara.eventhub.notification.application.NotificationService;
import com.enesincekara.eventhub.notification.domain.Notification;
import com.enesincekara.eventhub.user.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "user.registered",groupId = "eventhub-group")
    public void consume(UserRegisteredEvent event ) {

        Notification notification = new Notification(
                event.getEmail(),
                "Hoş geldiniz! Kaydınız başarıyla tamamlandı 🎉"
        );
        notificationService.send(notification);
        System.out.println("Received notification: " + notification);
    }
}
