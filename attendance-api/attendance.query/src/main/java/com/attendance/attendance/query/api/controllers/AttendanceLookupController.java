package com.attendance.attendance.query.api.controllers;

import com.attendance.attendance.query.api.dto.AttendanceLookupResponse;
import com.attendance.attendance.query.api.queries.FindAllEmployeeAttendanceQuery;
import com.attendance.attendance.query.api.queries.FindAttendanceByEmpIdAndDate;
import com.attendance.attendance.query.domain.Attendance;
import com.attendance.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/query/")
public class AttendanceLookupController {
    private final Logger logger = Logger.getLogger(AttendanceLookupController.class.getName());

    @Autowired
    private QueryDispatcher queryDispatcher;

    @GetMapping
    public String test(){
        return "WORKING";
    }

    @GetMapping(path = "/v1/attendanceLookup")
    public ResponseEntity<AttendanceLookupResponse> getAllAttendance() {
        try {
            List<Attendance> attendances = queryDispatcher.send(new FindAllEmployeeAttendanceQuery());
            if (attendances == null || attendances.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AttendanceLookupResponse.builder()
                    .attendances(attendances)
                    .message(MessageFormat.format("Successfully returned {0} bank account(s)!", attendances.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get all attendances request!";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AttendanceLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/v1/attendanceLookup/byId/{id}/{date}")
    public ResponseEntity<AttendanceLookupResponse> getAccountById(@PathVariable(value = "id") String id,@PathVariable(value = "date") String date) {
        try {
            List<Attendance> attendances = queryDispatcher.send(new FindAttendanceByEmpIdAndDate(id,date));
            if (attendances == null || attendances.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AttendanceLookupResponse.builder()
                    .attendances(attendances)
                    .message("Successfully returned bank account!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get attendances by ID request!";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AttendanceLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
