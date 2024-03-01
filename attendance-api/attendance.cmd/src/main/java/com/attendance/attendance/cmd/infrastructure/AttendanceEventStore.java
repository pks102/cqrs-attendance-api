package com.attendance.attendance.cmd.infrastructure;

import com.attendance.attendance.cmd.domain.AttendanceAggregate;
import com.attendance.attendance.cmd.domain.EventStoreRepository;
import com.attendance.cqrs.core.domain.AggregateRoot;
import com.attendance.cqrs.core.events.BaseEvent;
import com.attendance.cqrs.core.events.EventModel;
import com.attendance.cqrs.core.exceptions.AggregateNotFoundException;
import com.attendance.cqrs.core.exceptions.ConcurrentException;
import com.attendance.cqrs.core.infrastructure.EventStore;
import com.attendance.cqrs.core.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Autowired
    private EventProducer eventProducer;

    @Value("${spring.kafka.topic}")
    private String topic;
    @Override
    public void saveEvents(AggregateRoot root, Iterable<BaseEvent> events, int expectedVersion, boolean publishToKafka) {
        AttendanceAggregate attendanceAggregate = (AttendanceAggregate) root;
        List<EventModel> occurredEvents = eventStoreRepository.findByEmpIdAndDate(attendanceAggregate.getEmpId(),attendanceAggregate.getDate());

        if(expectedVersion!=-1 && occurredEvents.get(occurredEvents.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrentException();
        }
        int version = expectedVersion;
        for (BaseEvent event: events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder()
                    .eventData(event)
                    .eventType(event.getClass().getName())
                    .aggregateIdentifier(attendanceAggregate.getId())
                    .aggregateType(AttendanceAggregate.class.getTypeName())
                    .timestamp(new Date())
                    .version(version)
                    .build();
            EventModel savedEvent = eventStoreRepository.save(eventModel);
            if(!savedEvent.getId().isEmpty() && publishToKafka) {
                //TODO produce event to kafka
                eventProducer.publish(topic,event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> occurredEvents = eventStoreRepository.findByAggregateIdentifier(aggregateId);
//        if(occurredEvents==null || occurredEvents.isEmpty()) {
//            throw new AggregateNotFoundException("Incorrect aggreageId provided!");
//        }
        return occurredEvents.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

    @Override
    public List<BaseEvent> getByEmpIdAndDate(String empId, LocalDate date) {
        List<EventModel> occurredEvents = eventStoreRepository.findByEmpIdAndDate(empId,date);
        return occurredEvents.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

//    @Override
//    public List<BaseEvent> getEventsByEmpIdAndDate(String empId, LocalDate date) {
//        List<EventModel> eventsByEmpIdAndDate = eventStoreRepository.getEventsByEmpIdAndDate(empId, date);
//
//        return eventsByEmpIdAndDate.stream().map(EventModel::getEventData).collect(Collectors.toList());
//    }

//    public List<BaseEvent> getEventsByAggregateIdAndEmpId(String aggregateId,String empId) {
//        List<EventModel> occurredEvents = eventStoreRepository.findByAggregateIdentifierAndEmpId(aggregateId,empId);
//        if(occurredEvents==null || occurredEvents.isEmpty()) {
//            throw new AggregateNotFoundException("Incorrect aggreageId provided!");
//        }
//        return occurredEvents.stream().map(EventModel::getEventData).collect(Collectors.toList());
//    }

    @Override
    public List<String> getAggregateIds() {
        List<EventModel> eventStream = eventStoreRepository.findAll();
        if (eventStream == null || eventStream.isEmpty()) {
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }


}
