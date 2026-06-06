package com.devmasters.restaurant_erp.attendance.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "attendance")
public class Attendance extends BaseDomain {

    private String employeeId;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private Double workingHours;
}