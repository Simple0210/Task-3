package com.example.employeeturniketapp.service;

import com.example.employeeturniketapp.entity.Employee;
import com.example.employeeturniketapp.entity.temp.ApiResult;
import com.example.employeeturniketapp.payload.reqDto.EmployeeReqDTO;
import com.example.employeeturniketapp.payload.resDto.EmployeeResDTO;
import com.example.employeeturniketapp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public ApiResult getAllEmployee() {
        List<EmployeeResDTO> employeeList = employeeRepository.findAll().stream().map(this::castEmpToEmpResDto).collect(Collectors.toList());
        return new ApiResult(employeeList, true);
    }

    @Override
    public ApiResult getEmployeeById(Integer id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            EmployeeResDTO employeeResDTO = castEmpToEmpResDto(optionalEmployee.get());
            return new ApiResult(employeeResDTO, true);
        }
        return new ApiResult("Bunday employee topilmadi", false);
    }

    @Override
    public ApiResult addNewEmployee(EmployeeReqDTO employeeReqDTO) {
        Employee employee = castEmpReqDtoToEmp(employeeReqDTO);
        employeeRepository.save(employee);
        return new ApiResult("Employee muvaffaqiyatli saqlandi", true);
    }

    @Override
    public ApiResult editEmployee(Integer id, EmployeeReqDTO employeeReqDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setFirstname(employeeReqDTO.getFirstname());
            employee.setLastname(employeeReqDTO.getLastname());
            employee.setPhoneNumber(employeeReqDTO.getPhoneNumber());
            employeeRepository.save(employee);
            return new ApiResult("Employee ma`lumotlari muvaffaqiyatli taxrirlandi", true);
        } else {
            return new ApiResult("Bunday employee topilmadi", false);
        }
    }

    @Override
    public ApiResult deleteEmployee(Integer id) {
        try {
            employeeRepository.deleteById(id);
            return new ApiResult("Employee o`chirildi", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult("Employee o`chirilmadi", false);
        }
    }

    /*
    REQUESTDA KELGAN EMPLOYEEREQDTONI EMPLOYEEGA O`GIRIB QAYTARADI
     */
    public Employee castEmpReqDtoToEmp(EmployeeReqDTO employeeReqDTO) {
        Employee employee = new Employee();
        employee.setFirstname(employeeReqDTO.getFirstname());
        employee.setLastname(employeeReqDTO.getLastname());
        employee.setPhoneNumber(employeeReqDTO.getPhoneNumber());
        return employee;
    }

    /*
    METODGA BERILGAN EMPLOYEENI FRONTGA JO`NATISH UCHUN EMPLOYEERESDTOGA O`GIRUVCHI METOD
     */
    public EmployeeResDTO castEmpToEmpResDto(Employee employee) {
        EmployeeResDTO employeeResDTO = new EmployeeResDTO();
        employeeResDTO.setId(employee.getId());
        employeeResDTO.setFirstname(employee.getFirstname());
        employeeResDTO.setLastname(employee.getLastname());
        employeeResDTO.setPhoneNumber(employee.getPhoneNumber());
        return employeeResDTO;
    }
}
