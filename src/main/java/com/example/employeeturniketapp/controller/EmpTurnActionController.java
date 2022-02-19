package com.example.employeeturniketapp.controller;

import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.DateAndTimeDto;
import com.example.employeeturniketapp.payload.reqDto.EmpTurnActionReqDTO;
import com.example.employeeturniketapp.service.EmpTurnActionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/action")
@Validated
public class EmpTurnActionController {

    private final EmpTurnActionServiceImpl empTurnActionService;

    @GetMapping("/getAllEmpTurnAction")
    public ApiResult getAllEmpTurnAction() {
        return empTurnActionService.getAllEmpTurnAction();
    }

    @GetMapping("/getEmpTurnActionById/{id}")
    public ApiResult getEmpTurnActionById(@PathVariable Integer id) {
        return empTurnActionService.getEmpTurnActionById(id);
    }

    @PostMapping("/addNewEmpTurnAction")
    public ApiResult addNewEmpTurnAction(@Valid @RequestBody EmpTurnActionReqDTO empTurnActionReqDTO) {
        return empTurnActionService.addNewEmpTurnAction(empTurnActionReqDTO);
    }

    @PutMapping("/editEmpTurnAction/{id}")
    public ApiResult editEmpTurnAction(@PathVariable Integer id, @Valid @RequestBody EmpTurnActionReqDTO empTurnActionReqDTO) {
        return empTurnActionService.editEmpTurnAction(id, empTurnActionReqDTO);
    }

    @DeleteMapping("/deleteEmpTurnAction/{id}")
    public ApiResult deleteEmpTurnAction(@PathVariable Integer id) {
        return empTurnActionService.deleteEmpTurnAction(id);
    }

    @PostMapping("/getAllEmpTurnActionByEmployee")
    public ApiResult getAllIncomeOrOutcomeEmpTurnActionByEmployeeId(@Valid @RequestBody EmpTurnActionReqDTO empTurnActionReqDTO) {
        return empTurnActionService.getAllIncomeOrOutcomeEmpTurnActionByEmployee(empTurnActionReqDTO);
    }

    @PostMapping("/getEmployeesBeforeIncomeOrBeforeOutcome")
    public ResponseEntity<Resource> getEmployeesBeforeIncomeOrBeforeOutcome(@RequestBody DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException {
        return empTurnActionService.getEmployeesBeforeIncomeOrBeforeOutcome(dateAndTimeDto);
    }

    @PostMapping("/getEmployeesAfterIncomeOrAfterOutcome")
    public ResponseEntity<Resource> getEmployeesAfterIncomeOrAfterOutcome(@RequestBody DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException {
        return empTurnActionService.getEmployeesAfterIncomeOrAfterOutcome(dateAndTimeDto);
    }
}
