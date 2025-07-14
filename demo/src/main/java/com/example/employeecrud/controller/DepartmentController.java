package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Department;
import com.example.employeecrud.dto.DepartmentDtoList;
import com.example.employeecrud.repository.DepartmentRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepo departmentRepo;

    @PostMapping("/add")
    public ResponseEntity<Department> addDepartment(@Valid @RequestBody Department department) {
        Department saved = departmentRepo.save(department);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<Department>> addAllDepartments(@Valid @RequestBody DepartmentDtoList departmentList) {
        List<Department> savedDepartments = departmentRepo.saveAll(departmentList.getDepartments());
        return ResponseEntity.ok(savedDepartments);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> list = departmentRepo.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department updatedDept) {
        return departmentRepo.findById(id)
                .map(dept -> {
                    dept.setDeptName(updatedDept.getDeptName());
                    Department saved = departmentRepo.save(dept);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (departmentRepo.existsById(id)) {
            departmentRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
