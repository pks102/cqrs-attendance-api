package com.attendance.attendance.cmd.api.commands;

import com.attendance.attendance.cmd.domain.AttendanceAggregate;
import com.attendance.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class AttendanceHandler implements CommandHandler{

    @Autowired
    private EventSourcingHandler<AttendanceAggregate> eventSourcingHandler;
    @Override
    public void handle(SwipeInCommand command) {

        System.out.println("local data UTC swipe in date : "+ LocalDate.now(ZoneId.of("UTC")));
        System.out.println("local date Asia swipe in date : "+ LocalDate.now(ZoneId.of("Asia/Kolkata")));
        System.out.println("Zonedate UTC swipe in date : "+ ZonedDateTime.now(ZoneId.of("UTC")));
        AttendanceAggregate aggregate = eventSourcingHandler.getByEmpIdAndDate(command.getEmpId(), LocalDate.now());
        aggregate.swipeIn(command);
        eventSourcingHandler.saveAggregate(aggregate,false);
    }

    @Override
    public void handle(SwipeOutCommand command) {
        System.out.println("swipe out date : "+LocalDate.now());

        AttendanceAggregate aggregate = eventSourcingHandler.getByEmpIdAndDate(command.getEmpId(),LocalDate.now());
        aggregate.swipeOut(command);
        eventSourcingHandler.saveAggregate(aggregate, false);
    }

    @Override
    public void handle(EodAttendanceCommand command) {
        System.out.println("eod attendace : "+LocalDate.now());

        AttendanceAggregate aggregate = eventSourcingHandler.getByEmpIdAndDate(command.getEmpId(),LocalDate.now());
        aggregate.performEodAttendance(command);
        eventSourcingHandler.saveAggregate(aggregate, true);
    }
}
