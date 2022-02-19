package com.example.employeeturniketapp.controller;

import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.EmployeeReqDTO;
import com.example.employeeturniketapp.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;


    @GetMapping("/getAllEmployee")
    public ApiResult getAllEmployee() {
        return employeeServiceImpl.getAllEmployee();
    }

    @GetMapping("/getEmployeeById/{id}")
    public ApiResult getEmployeeById(@PathVariable Integer id) {
        return employeeServiceImpl.getEmployeeById(id);
    }

    @PostMapping("/addNewEmployee")
    public ApiResult addNewEmployee(@Valid @RequestBody EmployeeReqDTO employeeReqDTO) {
        return employeeServiceImpl.addNewEmployee(employeeReqDTO);
    }

    @PutMapping("/editEmployee/{id}")
    public ApiResult editEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeReqDTO employeeReqDTO) {
        return employeeServiceImpl.editEmployee(id, employeeReqDTO);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ApiResult deleteEmployee(@PathVariable Integer id) {
        return employeeServiceImpl.deleteEmployee(id);
    }


}
