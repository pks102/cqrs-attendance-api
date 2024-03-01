package com.attendance.attendance.cmd.api.commands;

public interface CommandHandler {
    void handle(SwipeInCommand command);
    void handle(SwipeOutCommand command);
    void handle(EodAttendanceCommand command);

}
