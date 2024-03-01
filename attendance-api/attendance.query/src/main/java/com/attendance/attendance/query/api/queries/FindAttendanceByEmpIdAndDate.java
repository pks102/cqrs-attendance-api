package com.attendance.attendance.query.api.queries;

import com.attendance.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAttendanceByEmpIdAndDate extends BaseQuery {
private String empId;
private String date;

}
