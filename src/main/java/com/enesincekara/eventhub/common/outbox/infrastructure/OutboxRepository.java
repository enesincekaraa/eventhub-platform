package com.enesincekara.eventhub.common.outbox.infrastructure;

import com.enesincekara.eventhub.common.outbox.domain.OutboxEvent;
import com.enesincekara.eventhub.common.outbox.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatus(OutboxStatus status);
}

