package com.example.employeecrud.service;

import com.example.employeecrud.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    DepartmentDto addDepartment(DepartmentDto dto);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentById(Long deptId);
    DepartmentDto updateDepartment(Long deptId, DepartmentDto dto);
    void deleteDepartment(Long deptId);
}
