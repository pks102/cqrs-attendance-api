package com.attendance.attendance.cmd.infrastructure;

import com.attendance.attendance.cmd.domain.AttendanceAggregate;
import com.attendance.cqrs.core.domain.AggregateRoot;
import com.attendance.cqrs.core.events.BaseEvent;
import com.attendance.cqrs.core.handlers.EventSourcingHandler;
import com.attendance.cqrs.core.infrastructure.EventStore;
import com.attendance.cqrs.core.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceEventSourcingHandler implements EventSourcingHandler<AttendanceAggregate> {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventProducer eventProducer;

    @Value("${spring.kafka.topic}")
    private String topic;


    @Override
    public void saveAggregate(AggregateRoot aggregateRoot, boolean publishToKafka) {
        eventStore.saveEvents(aggregateRoot, aggregateRoot.getUncommittedChanges(),aggregateRoot.getVersion(),publishToKafka);
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AttendanceAggregate getByEmpIdAndDate(String empId, LocalDate date) {
        AttendanceAggregate aggregateRoot = new AttendanceAggregate();
        List<BaseEvent> events = eventStore.getByEmpIdAndDate(empId,date);
        if (events!=null && !events.isEmpty()) {
            aggregateRoot.replayEvents(events);
            Optional<Integer> max = events.stream().map(baseEvent -> baseEvent.getVersion()).max(Comparator.naturalOrder());
            aggregateRoot.setVersion(max.get());
        }
        return aggregateRoot;
    }

    @Override
    public AttendanceAggregate getById(String id) {
        AttendanceAggregate aggregateRoot = new AttendanceAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);
        if (events!=null && !events.isEmpty()) {
            aggregateRoot.replayEvents(events);
            Optional<Integer> max = events.stream().map(baseEvent -> baseEvent.getVersion()).max(Comparator.naturalOrder());
            aggregateRoot.setVersion(max.get());
        }
        return aggregateRoot;
    }

}
