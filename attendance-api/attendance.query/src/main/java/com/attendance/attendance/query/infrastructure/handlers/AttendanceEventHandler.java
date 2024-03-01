package com.attendance.attendance.query.infrastructure.handlers;

import com.attendance.attendance.common.events.EodAttendanceEvent;
import com.attendance.attendance.common.events.SwipeInEvent;
import com.attendance.attendance.common.events.SwipeOutEvent;
import com.attendance.attendance.query.domain.Attendance;
import com.attendance.attendance.query.domain.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AttendanceEventHandler implements EventHandler {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public void on(EodAttendanceEvent event) {
        Attendance attendance = new Attendance();
        attendance.setEmpId(event.getEmpId());
        attendance.setDate(event.getDate());
        attendance.setStatus(event.getStatus());
        attendanceRepository.save(attendance);
    }

}
