package com.attendance.cqrs.core.infrastructure;

import com.attendance.cqrs.core.commands.BaseCommand;
import com.attendance.cqrs.core.commands.CommandHandlerMethod;


//Interface used to register command handlers and to send or dispatch commands
public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
