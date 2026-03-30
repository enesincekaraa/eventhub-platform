package com.enesincekara.eventhub.common.outbox.infrastructure;

import com.enesincekara.eventhub.common.event.EventTopics;
import com.enesincekara.eventhub.common.kafka.KafkaProducer;
import com.enesincekara.eventhub.common.outbox.constants.OutboxConstants;
import com.enesincekara.eventhub.common.outbox.domain.OutboxEvent;
import com.enesincekara.eventhub.user.domain.User;
import com.enesincekara.eventhub.user.domain.event.UserRegisteredEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publish() {
        List<OutboxEvent> outboxEvents = outboxRepository.findByProcessedFalse();
        if (outboxEvents.isEmpty()){
            return;
        }

        log.info("{} adet bekleyen event bulundu. Gönderim başlıyor...", outboxEvents.size());

        for (OutboxEvent outboxEvent : outboxEvents) {
            try {

                UserRegisteredEvent event = convertPayload(outboxEvent.getPayload(), UserRegisteredEvent.class);

                kafkaProducer.sendMessage(EventTopics.USER_REGISTERED, event);

                outboxEvent.markProcessed();

                outboxRepository.save(outboxEvent);

                log.info("Event başarıyla gönderildi: {}", outboxEvent.getId());
            }

            catch (Exception e) {
                outboxEvent.increaseRetry();
                if (outboxEvent.getRetryCount()>= OutboxConstants.MAX_RETRY){
                    log.error("Event DLQ'ya gönderiliyor : {}", outboxEvent.getId());
                    kafkaProducer.sendMessage(EventTopics.USER_REGISTERED_DLQ, outboxEvent.getPayload());
                    outboxEvent.markProcessed();
                }
                else {
                    log.warn("Retry {} for event {}", outboxEvent.getRetryCount(), outboxEvent.getId());
                }
            }
        }
    }


    private <T> T  convertPayload(String payload,Class<T> targetClass) {
        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (JsonProcessingException e) {
            log.error("Payload {} sınıfına dönüştürülürken hata oluştu!", targetClass.getSimpleName(), e);
            throw new RuntimeException("JSON dönüşüm hatası: " + e.getMessage());
        }

    }
}
