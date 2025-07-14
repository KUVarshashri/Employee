package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.Department;
import com.example.employeecrud.dto.DepartmentDto;
import com.example.employeecrud.repository.DepartmentRepo;
import com.example.employeecrud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    private DepartmentDto convertToDto(Department dept) {
        DepartmentDto dto = new DepartmentDto();
        dto.setDeptId(dept.getDeptId());
        dto.setDeptName(dept.getDeptName());
        return dto;
    }

    private Department convertToEntity(DepartmentDto dto) {
        Department dept = new Department();
        dept.setDeptId(dto.getDeptId());
        dept.setDeptName(dto.getDeptName());
        return dept;
    }

    @Override
    public DepartmentDto addDepartment(DepartmentDto dto) {
        Department saved = departmentRepo.save(convertToEntity(dto));
        return convertToDto(saved);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getDepartmentById(Long deptId) {
        return departmentRepo.findById(deptId).map(this::convertToDto).orElse(null);
    }

    @Override
    public DepartmentDto updateDepartment(Long deptId, DepartmentDto dto) {
        Department existing = departmentRepo.findById(deptId).orElse(null);
        if (existing != null) {
            existing.setDeptName(dto.getDeptName());
            return convertToDto(departmentRepo.save(existing));
        }
        return null;
    }

    @Override
    public void deleteDepartment(Long deptId) {
        departmentRepo.deleteById(deptId);
    }
}
