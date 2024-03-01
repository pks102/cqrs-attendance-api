package com.attendance.attendance.cmd.domain;

import com.attendance.cqrs.core.events.EventModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventStoreRepository extends MongoRepository<EventModel,String> {

    List<EventModel> findByAggregateIdentifier(String aggregateId);
    @Query("{'eventData.empId': ?0,'eventData.date':?1}")
    List<EventModel> findByEmpIdAndDate(String empId,LocalDate date);
//    List<EventModel> findByAggregateIdentifierAndEmpId(String aggregateId,String empId);

//    List<EventModel> getEventsByEmpIdAndDate(String empId, LocalDate date);
}
