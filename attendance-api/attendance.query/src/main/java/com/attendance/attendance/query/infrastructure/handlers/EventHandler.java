package com.attendance.attendance.query.infrastructure.handlers;

import com.attendance.attendance.common.events.EodAttendanceEvent;
import com.attendance.attendance.common.events.SwipeInEvent;
import com.attendance.attendance.common.events.SwipeOutEvent;

public interface EventHandler {
//    void on(SwipeInEvent event);
//
//    void on(SwipeOutEvent event);

     void on(EodAttendanceEvent event);
}
