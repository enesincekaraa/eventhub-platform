package com.enesincekara.eventhub.common.outbox;

import com.enesincekara.eventhub.common.outbox.domain.OutboxStatus;
import com.enesincekara.eventhub.common.outbox.infrastructure.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/outbox")
public class OutboxAdminController {
    private final OutboxRepository outboxRepository;

    @PostMapping("/{id}/retry")
    public ResponseEntity<String> retryEvent(@PathVariable UUID id) {
        return outboxRepository.findById(id).map(event -> {
            if (event.getStatus() != OutboxStatus.FAILED) {
                return ResponseEntity.badRequest().body("Sadece FAILED olanlar retry edilebilir.");
            }
            event.setStatus(OutboxStatus.PENDING); // Tekrar sıraya al
            outboxRepository.save(event);
            return ResponseEntity.ok("Event tekrar sıraya alındı. 50 saniye içinde gönderilecek.");
        }).orElse(ResponseEntity.notFound().build());
    }
}
