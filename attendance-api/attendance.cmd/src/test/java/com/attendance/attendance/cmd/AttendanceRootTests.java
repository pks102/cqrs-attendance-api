package com.attendance.attendance.cmd;

import com.attendance.attendance.cmd.api.commands.EodAttendanceCommand;
import com.attendance.attendance.cmd.api.commands.SwipeInCommand;
import com.attendance.attendance.cmd.api.commands.SwipeOutCommand;
import com.attendance.attendance.cmd.domain.AttendanceAggregate;
import com.attendance.attendance.common.events.SwipeOutEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttendanceRootTests {

    @Test
    public void testSwipeIn() {
        AttendanceAggregate aggregate = new AttendanceAggregate();
        SwipeInCommand command = new SwipeInCommand("testEmpId");

        aggregate.swipeIn(command);

        assertEquals("testEmpId", aggregate.getEmpId());
        assertEquals(LocalDate.now(), aggregate.getDate());
        assertNotNull(aggregate.getSwipeInTime());
    }

    @Test
    public void testSwipeOut() {
        AttendanceAggregate aggregate = new AttendanceAggregate();
        SwipeInCommand swipeInCommand = new SwipeInCommand("testEmpId");
        aggregate.swipeIn(swipeInCommand);

        SwipeOutCommand swipeOutCommand = new SwipeOutCommand("testEmpId");
        aggregate.swipeOut(swipeOutCommand);

        assertEquals("testEmpId", aggregate.getEmpId());
        assertEquals(LocalDate.now(), aggregate.getDate());
        assertNotNull(aggregate.getSwipeOutTime());
    }

    @Test
    public void testApplySwipeOutEventWithoutSwipeIn() {
        AttendanceAggregate aggregate = new AttendanceAggregate();

        // Do not call swipeIn, so that swipeInTime remains null

        SwipeOutEvent event = SwipeOutEvent.builder()
                .id("testEventId")
                .empId("testEmpId")
                .swipeOutTime(LocalTime.now())
                .date(LocalDate.now())
                .build();

        // Expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> aggregate.apply(event), "swipeIn time not present and swipeOut performed");
    }

    @Test
    public void testPerformEodAttendance() {
        AttendanceAggregate aggregate = new AttendanceAggregate();
        SwipeInCommand swipeInCommand = new SwipeInCommand("testEmpId");
        aggregate.swipeIn(swipeInCommand);

        SwipeOutCommand swipeOutCommand = new SwipeOutCommand("testEmpId");
        aggregate.swipeOut(swipeOutCommand);

        EodAttendanceCommand eodCommand = new EodAttendanceCommand("testEmpId");
        aggregate.performEodAttendance(eodCommand);

        assertNotNull(aggregate.getAttendanceStatus());
    }
}
