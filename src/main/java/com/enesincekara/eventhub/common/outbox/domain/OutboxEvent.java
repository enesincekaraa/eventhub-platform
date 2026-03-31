package com.enesincekara.eventhub.common.outbox.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "outbox")
@Data
public class OutboxEvent {
    @Id
    private UUID id;
    private String aggregateType;
    private String aggregateId;
    private String type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    private String errorMessage;

    private int retryCount;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateType, String aggregateId, String type, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
    }


    public void markProcessed() {
        this.status = OutboxStatus.PROCESSED;
        this.errorMessage = null;
    }

    public void markFailed(String error) {
        this.status = OutboxStatus.FAILED;
        this.errorMessage = error;
    }
    public void increaseRetry(){
        this.retryCount++;
    }

}
