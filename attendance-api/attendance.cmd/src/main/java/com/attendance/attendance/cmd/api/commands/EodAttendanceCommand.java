package com.attendance.attendance.cmd.api.commands;

import com.attendance.cqrs.core.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EodAttendanceCommand extends BaseCommand {
    String empId;
}
