package com.example.employeeturniketapp.service;

import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.EmployeeReqDTO;

public interface EmployeeService {

    ApiResult getAllEmployee();

    ApiResult getEmployeeById(Integer id);

    ApiResult addNewEmployee(EmployeeReqDTO employeeReqDTO);

    ApiResult editEmployee(Integer id, EmployeeReqDTO employeeReqDTO);

    ApiResult deleteEmployee(Integer id);

}
