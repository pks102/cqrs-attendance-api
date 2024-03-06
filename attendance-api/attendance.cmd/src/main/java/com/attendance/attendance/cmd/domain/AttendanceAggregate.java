package com.attendance.attendance.cmd.domain;

import com.attendance.attendance.cmd.api.commands.EodAttendanceCommand;
import com.attendance.attendance.cmd.api.commands.SwipeInCommand;
import com.attendance.attendance.cmd.api.commands.SwipeOutCommand;
import com.attendance.attendance.common.dto.AttendanceStatus;
import com.attendance.attendance.common.events.EodAttendanceEvent;
import com.attendance.attendance.common.events.SwipeInEvent;
import com.attendance.attendance.common.events.SwipeOutEvent;
import com.attendance.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.util.concurrent.TimeUnit.HOURS;

@NoArgsConstructor
public class AttendanceAggregate extends AggregateRoot {

    private String empId;

    private LocalDate date;
    private LocalTime swipeInTime;
    private LocalTime swipeOutTime;

    private Long timeInPremise;

    private Long totalTimeInPremise = 0L;
    private AttendanceStatus attendanceStatus;


    public String getEmpId() {
        return empId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getSwipeInTime() {
        return swipeInTime;
    }

    public LocalTime getSwipeOutTime() {
        return swipeOutTime;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void swipeIn(SwipeInCommand command) {
        raiseEvent(SwipeInEvent.builder()
                .id(command.getId())
                .empId(command.getEmpId())
                .swipeInTime(LocalTime.now())
                .date(LocalDate.now())
                .build());
    }

    public void apply(SwipeInEvent event) {
        this.id = event.getId();
        this.empId = event.getEmpId();
        this.date = event.getDate();
        this.swipeInTime = event.getSwipeInTime();
    }

    public void swipeOut(SwipeOutCommand command) {
        raiseEvent(SwipeOutEvent.builder()
                .id(command.getId())
                .empId(command.getEmpId())
                .swipeOutTime(LocalTime.now())
                .date(LocalDate.now())
                .build());
    }

    public void apply(SwipeOutEvent event) {
        if(this.swipeInTime==null) throw new RuntimeException("swipeIn time not present and swipeOut performed");
        this.id = event.getId();
        this.empId = event.getEmpId();
        this.date = event.getDate();
        this.swipeOutTime = event.getSwipeOutTime();
        this.timeInPremise = Duration.between(this.getSwipeInTime(), this.getSwipeOutTime()).toSeconds();
        this.totalTimeInPremise += this.timeInPremise;
    }


    public void performEodAttendance(EodAttendanceCommand command) {
        Long totalHours = this.totalTimeInPremise / 3600;
        AttendanceStatus status = null;
        if(totalHours < 4)  {
            status = AttendanceStatus.ABSENT;
        } else if (totalHours > 4 && totalHours < 8) {
            status = AttendanceStatus.HALFDAY;
        } else {
            status = AttendanceStatus.PRESENT;
        }
        raiseEvent(EodAttendanceEvent.builder()
                .empId(command.getEmpId())
                .date(LocalDate.now())
                .totalHours(totalHours)
                .status(status)
                .build());
    }

    public void apply(EodAttendanceEvent event) {
        this.id = event.getId();
        this.empId = event.getEmpId();
        this.date = event.getDate();
        this.attendanceStatus = event.getStatus();
    }
}
