package com.attendance.cqrs.core.producer;

import com.attendance.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void publish(String topic, BaseEvent event);
}
