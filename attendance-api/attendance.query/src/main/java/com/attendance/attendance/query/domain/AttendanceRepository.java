package com.attendance.attendance.query.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
    Optional<Attendance> findByEmpIdAndDate(String empId, LocalDate date);
}
