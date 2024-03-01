package com.attendance.attendance.query;

import com.attendance.attendance.query.api.queries.FindAllEmployeeAttendanceQuery;
import com.attendance.attendance.query.api.queries.FindAttendanceByEmpIdAndDate;
import com.attendance.attendance.query.api.queries.QueryHandler;
import com.attendance.cqrs.core.infrastructure.QueryDispatcher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryApplication {
	@Autowired
	private QueryDispatcher queryDispatcher;

	@Autowired
	private QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHandler(FindAllEmployeeAttendanceQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAttendanceByEmpIdAndDate.class, queryHandler::handle);
//		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
////		queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
	}
}
