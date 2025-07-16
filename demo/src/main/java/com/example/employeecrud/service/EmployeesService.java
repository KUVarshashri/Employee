package com.example.employeecrud.service;

import com.example.employeecrud.dto.EmployeesDto;

import java.util.List;
import java.util.Set;

public interface EmployeesService {
    EmployeesDto addEmployee(EmployeesDto dto);
    List<EmployeesDto> addAllEmployees(List<EmployeesDto> dtoList);
    List<EmployeesDto> getAllEmployees();
    EmployeesDto getEmployeeById(Long empId);
    EmployeesDto updateEmployee(Long empId, EmployeesDto dto);
    void deleteEmployee(Long empId);

    EmployeesDto assignProjects(Long empId, Set<String> projectNames);
}
