package com.attendance.cqrs.core.infrastructure;

import com.attendance.cqrs.core.domain.AggregateRoot;
import com.attendance.cqrs.core.events.BaseEvent;

import java.time.LocalDate;
import java.util.List;

public interface EventStore {
    void saveEvents(AggregateRoot aggregateRoot, Iterable<BaseEvent> events, int expectedVersion, boolean publishToKafka);
    List<BaseEvent> getEvents(String aggregateId);

    List<BaseEvent> getByEmpIdAndDate(String empId, LocalDate date);
    List<String> getAggregateIds();
    
}
