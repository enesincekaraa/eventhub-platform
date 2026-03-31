package com.enesincekara.eventhub.common.outbox.infrastructure;

import com.enesincekara.eventhub.common.event.EventTopics;
import com.enesincekara.eventhub.common.kafka.KafkaProducer;
import com.enesincekara.eventhub.common.outbox.constants.OutboxConstants;
import com.enesincekara.eventhub.common.outbox.domain.OutboxEvent;
import com.enesincekara.eventhub.common.outbox.domain.OutboxStatus;
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


    @Scheduled(fixedRate = 50000)
    public void publish(){
        List<OutboxEvent> outboxEvents = outboxRepository.findByStatus(OutboxStatus.PENDING);

        for (OutboxEvent outboxEvent : outboxEvents) {
            try {
                UserRegisteredEvent event;

                try {
                    event=convertPayload(outboxEvent.getPayload(), UserRegisteredEvent.class);
                }catch (Exception e) {
                    log.error("Kritik Veri Hatası! Mesaj bozuk, DLQ'ya alınıyor: {}", outboxEvent.getId());
                    outboxEvent.markFailed(e.getMessage());
                    outboxRepository.save(outboxEvent);
                    sendAlarmVeriHatasi(outboxEvent,e.getMessage());
                    continue;
                }
                kafkaProducer.sendMessage(EventTopics.USER_REGISTERED,event);
                outboxEvent.markProcessed();
                outboxRepository.save(outboxEvent);
                log.info("Event başarıyla gönderildi: {}", outboxEvent.getId());
            }catch (Exception e){
                log.warn("Kafka'ya ulaşılamıyor, bir sonraki denemede tekrar denenecek: {}", outboxEvent.getId());
                sendAlarmKafkaHatasi(outboxEvent,e.getMessage());
                break;
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


    private void sendAlarmVeriHatasi(OutboxEvent outboxEvent,String reason){
        log.error("**************************************************");
        log.error("!!! ACİL DURUM: OUTBOX MESAJI ");
        log.error("ID: {}", outboxEvent.getId());
        log.error("Neden: {}", reason);
        log.error("**************************************************");
    }
    private void sendAlarmKafkaHatasi(OutboxEvent outboxEvent, String message) {
        log.error("**************************************************");
        log.error("!!! ACİL DURUM: OUTBOX MESAJI ");
        log.error("Kafkaya Ulaşılamıyor");
        log.error("ID: {}", outboxEvent.getId());
        log.error("Neden: {}", message);
        log.error("**************************************************");
    }
}
