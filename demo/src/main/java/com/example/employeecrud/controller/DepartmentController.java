package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Department;
import com.example.employeecrud.dto.DepartmentDtoList;
import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.repository.DepartmentRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private static final String DEPARTMENT_ADDED_SUCCESSFULLY = "Department added successfully";
    private static final String DEPARTMENTS_ADDED_SUCCESSFULLY = "Departments added successfully";
    private static final String DEPARTMENTS_FETCHED_SUCCESSFULLY = "Departments fetched successfully";
    private static final String DEPARTMENT_FOUND = "Department found";
    private static final String DEPARTMENT_NOT_FOUND = "Department not found";
    private static final String DEPARTMENT_UPDATED_SUCCESSFULLY = "Department updated successfully";
    private static final String DEPARTMENT_DELETED_SUCCESSFULLY = "Department deleted successfully";

    @Autowired
    private DepartmentRepo departmentRepo;

    @PostMapping("/add")
    public GenericResponseEntity<Department> addDepartment(@Valid @RequestBody Department department) {
        Department saved = departmentRepo.save(department);
        return GenericResponseEntity.<Department>builder()
                .message(DEPARTMENT_ADDED_SUCCESSFULLY)
                .data(saved)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @PostMapping("/addAll")
    public GenericResponseEntity<List<Department>> addAllDepartments(@Valid @RequestBody DepartmentDtoList departmentList) {
        List<Department> savedDepartments = departmentRepo.saveAll(departmentList.getDepartments());
        return GenericResponseEntity.<List<Department>>builder()
                .message(DEPARTMENTS_ADDED_SUCCESSFULLY)
                .data(savedDepartments)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public GenericResponseEntity<List<Department>> getAllDepartments() {
        List<Department> list = departmentRepo.findAll();
        return GenericResponseEntity.<List<Department>>builder()
                .message(DEPARTMENTS_FETCHED_SUCCESSFULLY)
                .data(list)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public GenericResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepo.findById(id)
                .map(dept -> GenericResponseEntity.<Department>builder()
                        .message(DEPARTMENT_FOUND)
                        .data(dept)
                        .statusCode(200)
                        .status("OK")
                        .success(true)
                        .build())
                .orElse(GenericResponseEntity.<Department>builder()
                        .message(DEPARTMENT_NOT_FOUND)
                        .data(null)
                        .statusCode(404)
                        .status("NOT_FOUND")
                        .success(false)
                        .build());
    }

    @PutMapping("/update/{id}")
    public GenericResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                              @Valid @RequestBody Department updatedDept) {
        return departmentRepo.findById(id)
                .map(existing -> {
                    existing.setDeptName(updatedDept.getDeptName());
                    Department saved = departmentRepo.save(existing);
                    return GenericResponseEntity.<Department>builder()
                            .message(DEPARTMENT_UPDATED_SUCCESSFULLY)
                            .data(saved)
                            .statusCode(200)
                            .status("OK")
                            .success(true)
                            .build();
                })
                .orElse(GenericResponseEntity.<Department>builder()
                        .message(DEPARTMENT_NOT_FOUND)
                        .data(null)
                        .statusCode(404)
                        .status("NOT_FOUND")
                        .success(false)
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public GenericResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (departmentRepo.existsById(id)) {
            departmentRepo.deleteById(id);
            return GenericResponseEntity.<Void>builder()
                    .message(DEPARTMENT_DELETED_SUCCESSFULLY)
                    .data(null)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<Void>builder()
                    .message(DEPARTMENT_NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }
}
