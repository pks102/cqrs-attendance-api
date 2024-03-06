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
