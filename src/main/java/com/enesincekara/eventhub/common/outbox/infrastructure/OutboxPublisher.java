package com.enesincekara.eventhub.common.outbox.infrastructure;

import com.enesincekara.eventhub.common.kafka.KafkaProducer;
import com.enesincekara.eventhub.common.outbox.domain.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedRate = 5000)
    public void publish() {
        List<OutboxEvent> outboxEvents = outboxRepository.findByProcessedFalse();
        if (outboxEvents.isEmpty()){
            return;
        }

        log.info("{} adet bekleyen event bulundu. Gönderim başlıyor...", outboxEvents.size());

        for (OutboxEvent outboxEvent : outboxEvents) {
            try {
                kafkaProducer.sendMessage("user.registered", outboxEvent.getPayload());
                outboxEvent.markProcessed();
                outboxRepository.save(outboxEvent);
                log.info("Event başarıyla gönderildi: {}", outboxEvent.getId());            }
            catch (Exception e) {
                log.error("Event gönderilirken hata oluştu! Event ID: {}. Hata: {}",
                        outboxEvent.getId(), e.getMessage());
            }
        }
    }
}
