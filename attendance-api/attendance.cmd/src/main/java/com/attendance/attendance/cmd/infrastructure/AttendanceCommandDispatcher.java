package com.attendance.attendance.cmd.infrastructure;

import com.attendance.cqrs.core.commands.BaseCommand;
import com.attendance.cqrs.core.commands.CommandHandlerMethod;
import com.attendance.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> map = new HashMap<>();
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        List<CommandHandlerMethod> commandHandlerMethods = map.computeIfAbsent(type, c -> new LinkedList<>());
        commandHandlerMethods.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandlerMethod> commandHandlerMethods = map.get(command.getClass());
        if (commandHandlerMethods==null || commandHandlerMethods.isEmpty()) {
            throw new RuntimeException("No command Handler was registered");
        }
        if(commandHandlerMethods.size() > 1) {
            throw new RuntimeException("Cannot send command to more than one command");
        }

        commandHandlerMethods.get(0).handle(command);
    }
}
