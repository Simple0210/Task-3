package com.example.employeeturniketapp.payload.reqDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpTurnActionReqDTO {


    private Boolean actionIncomeYokiOutCome;

    @Positive(message = "Employee Id bo`lishi shart")
    private Integer employeeId;
}
