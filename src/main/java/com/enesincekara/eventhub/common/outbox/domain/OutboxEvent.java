package com.enesincekara.eventhub.common.outbox.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private boolean processed;

    private int retryCount;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateType, String aggregateId, String type, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
        this.processed = false;
    }


    public void markProcessed() {
        this.processed = true;
    }
    public void increaseRetry(){
        this.retryCount++;
    }

}
