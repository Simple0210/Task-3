package com.example.employeeturniketapp.payload.reqDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateAndTimeDto {
    LocalDateTime localDateTime;
    private Boolean incomeOrOutcome;
}
