package com.attendance.attendance.query.api.queries;

import com.attendance.attendance.query.domain.Attendance;
import com.attendance.attendance.query.domain.AttendanceRepository;
import com.attendance.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceQueryHandler implements QueryHandler {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public List<BaseEntity> handle(FindAllEmployeeAttendanceQuery query) {
        Iterable<Attendance> attendances = attendanceRepository.findAll();
        List<BaseEntity> attendanceResult = new ArrayList<>();
        attendances.forEach(attendanceResult::add);
        return attendanceResult;
    }

    @Override
    public List<BaseEntity> handle(FindAttendanceByEmpIdAndDate query) {
        Optional<Attendance> attendance = attendanceRepository.findByEmpIdAndDate(query.getEmpId(), LocalDate.parse(query.getDate()));
        if (attendance.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseEntity> attendaceResult = new ArrayList<>();
        attendaceResult.add(attendance.get());
        return attendaceResult;
    }
//
//    @Override
//    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
//        Optional<BaseEntity> bankAccount = attendanceRepository.findByAccountHolder(query.getAccountHolder());
//        if (bankAccount.isPresent()) {
//            return null;
//        }
//        List<BaseEntity> bankAccountList = new ArrayList<>();
//        bankAccountList.add(bankAccount.get());
//        return bankAccountList;
//    }
//
//    @Override
//    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
//        List<BaseEntity> bankAccountsList = query.getEqualityType() == EqualityType.GREATER_THAN
//                ? attendanceRepository.findByBalanceGreaterThan(query.getBalance())
//                : attendanceRepository.findByBalanceLessThan(query.getBalance());
//        return bankAccountsList;
//    }
}
