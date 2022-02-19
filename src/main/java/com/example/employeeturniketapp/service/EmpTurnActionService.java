package com.example.employeeturniketapp.service;

import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.DateAndTimeDto;
import com.example.employeeturniketapp.payload.reqDto.EmpTurnActionReqDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface EmpTurnActionService {

    ApiResult getAllEmpTurnAction();

    ApiResult getEmpTurnActionById(Integer id);

    ApiResult addNewEmpTurnAction(EmpTurnActionReqDTO empTurnActionReqDTO);

    ApiResult editEmpTurnAction(Integer id, EmpTurnActionReqDTO empTurnActionReqDTO);

    ApiResult deleteEmpTurnAction(Integer id);

    ApiResult getAllIncomeOrOutcomeEmpTurnActionByEmployee(EmpTurnActionReqDTO empTurnActionReqDTO);

    ResponseEntity<Resource> getEmployeesBeforeIncomeOrBeforeOutcome(DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException;

    ResponseEntity<Resource> getEmployeesAfterIncomeOrAfterOutcome(DateAndTimeDto dateAndTimeDto) throws IOException, InvalidFormatException;
}
