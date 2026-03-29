package com.enesincekara.eventhub.notification.application;

import com.enesincekara.eventhub.notification.domain.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void send(Notification notification) {
        System.out.println("Sending notification: " + notification);
        System.out.println("Message: " + notification.getMessage());
    }
}
