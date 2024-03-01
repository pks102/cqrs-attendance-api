package com.attendance.attendance.cmd;

import com.attendance.attendance.cmd.api.commands.CommandHandler;
import com.attendance.attendance.cmd.api.commands.EodAttendanceCommand;
import com.attendance.attendance.cmd.api.commands.SwipeInCommand;
import com.attendance.attendance.cmd.api.commands.SwipeOutCommand;
import com.attendance.cqrs.core.commands.CommandHandlerMethod;
import com.attendance.cqrs.core.infrastructure.CommandDispatcher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CommandApplication {

	@Autowired
	CommandDispatcher commandDispatcher;

	@Autowired
	CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		CommandHandlerMethod<SwipeInCommand> c =
				command -> commandHandler.handle(command);
		commandDispatcher.registerHandler(SwipeInCommand.class, c);
		commandDispatcher.registerHandler(SwipeOutCommand.class, command -> commandHandler.handle(command));
		commandDispatcher.registerHandler(EodAttendanceCommand.class, command -> commandHandler.handle(command));
	}
}
