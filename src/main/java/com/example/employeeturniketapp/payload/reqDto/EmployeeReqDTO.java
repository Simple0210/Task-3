package com.example.employeeturniketapp.payload.reqDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReqDTO {

    @NotEmpty(message = "Employeening ismi bo`sh bo`lmasligi kerak!")
    private String firstname;

    @NotEmpty(message = "Employeening familiyasi bo`sh bo`lmasligi kerak!")
    private String lastname;

    @NotEmpty(message = "Employeening telefon raqami bo`sh bo`lmasligi kerak!")
    private String phoneNumber;
}
