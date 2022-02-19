package com.example.employeeturniketapp.payload.resDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String phoneNumber;
}
