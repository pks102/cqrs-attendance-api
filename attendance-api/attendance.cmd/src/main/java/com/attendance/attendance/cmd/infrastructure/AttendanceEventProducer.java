package com.attendance.attendance.cmd.infrastructure;

import com.attendance.cqrs.core.events.BaseEvent;
import com.attendance.cqrs.core.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AttendanceEventProducer implements EventProducer {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void publish(String topic, BaseEvent event) {
        kafkaTemplate.send(topic,event);
    }
}
