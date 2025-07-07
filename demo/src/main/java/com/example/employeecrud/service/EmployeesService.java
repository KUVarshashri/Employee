package com.example.employeecrud.service;

import com.example.employeecrud.dto.EmployeesDto;

import java.util.List;

public interface EmployeesService {
    EmployeesDto addEmployee(EmployeesDto dto);
    List<EmployeesDto> addAllEmployees(List<EmployeesDto> dtoList);
    List<EmployeesDto> getAllEmployees();
    EmployeesDto getEmployeeById(Long empId);
    EmployeesDto updateEmployee(Long empId, EmployeesDto dto);
    void deleteEmployee(Long empId);
}
