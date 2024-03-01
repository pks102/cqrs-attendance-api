package com.attendance.attendance.query.domain;

import com.attendance.attendance.common.dto.AttendanceStatus;
import com.attendance.cqrs.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"empId","date"})})
public class Attendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String empId;
    private LocalDate date;
    private AttendanceStatus status;
}
