package com.attendance.cqrs.core.handlers;

import com.attendance.cqrs.core.domain.AggregateRoot;
import com.attendance.cqrs.core.events.BaseEvent;

import java.time.LocalDate;
import java.util.List;

//used to save the events to event store and get the latest state of aggregate by replaying all events
// by aggregateIdentifier
public interface EventSourcingHandler<T> {
    void saveAggregate(AggregateRoot aggregateRoot, boolean publishToKafka);
    T getById(String id);
    T getByEmpIdAndDate(String empId,LocalDate date);

}
