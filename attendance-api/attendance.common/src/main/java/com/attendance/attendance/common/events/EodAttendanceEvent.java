package com.attendance.attendance.common.events;

import com.attendance.attendance.common.dto.AttendanceStatus;
import com.attendance.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EodAttendanceEvent extends BaseEvent {
    private String empId;
    private LocalDate date;
    private Long totalHours;
    private AttendanceStatus status;
}
