package com.attendance.attendance.query.api.dto;

import com.attendance.attendance.common.dto.BaseResponse;
import com.attendance.attendance.query.domain.Attendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLookupResponse extends BaseResponse {
    private List<Attendance> attendances;

    public AttendanceLookupResponse(String message) {
        super(message);
    }
}
