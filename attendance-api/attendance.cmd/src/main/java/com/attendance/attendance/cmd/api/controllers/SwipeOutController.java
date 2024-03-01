package com.attendance.attendance.cmd.api.controllers;

import com.attendance.attendance.cmd.api.commands.SwipeInCommand;
import com.attendance.attendance.cmd.api.commands.SwipeOutCommand;
import com.attendance.attendance.common.dto.BaseResponse;
import com.attendance.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/command")
public class SwipeOutController {
    private final Logger logger = Logger.getLogger(SwipeOutController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping("/v1/swipeOut")
    public ResponseEntity<BaseResponse> swipeOut(@RequestBody SwipeOutCommand command) {
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse(command.getEmpId() + " swiped out "), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String safeErrorMessage = "Error while processing request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

