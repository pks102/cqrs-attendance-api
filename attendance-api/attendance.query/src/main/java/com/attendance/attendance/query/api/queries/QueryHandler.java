package com.attendance.attendance.query.api.queries;

import com.attendance.cqrs.core.domain.BaseEntity;

import java.util.List;


public interface QueryHandler {
    List<BaseEntity> handle(FindAllEmployeeAttendanceQuery query);
    List<BaseEntity> handle(FindAttendanceByEmpIdAndDate query);
//    List<BaseEntity> handle(FindAccountByHolderQuery query);
//    List<BaseEntity> handle(FindAccountWithBalanceQuery query);
}
