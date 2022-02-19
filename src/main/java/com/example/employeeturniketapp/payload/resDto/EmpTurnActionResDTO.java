package com.example.employeeturniketapp.payload.resDto;

import com.example.employeeturniketapp.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpTurnActionResDTO {

    private Integer id;

    private Boolean actionIncomeYokiOutCome;

    private Timestamp createdAt;

    private Employee employee;
}
