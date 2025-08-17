package com.manegement.library.shared.infrastructure.messaging;

import com.manegement.library.shared.events.DomainEvent;

public interface EventPublisher {
    void publish(String topic, DomainEvent event);
    void publishAsync(String topic, DomainEvent event);
}
