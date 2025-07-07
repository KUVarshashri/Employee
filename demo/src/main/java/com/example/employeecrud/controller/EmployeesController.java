package com.example.employeecrud.controller;

import com.example.employeecrud.dto.EmployeesDto;
import com.example.employeecrud.dto.EmployeesDtoList;
import com.example.employeecrud.service.EmployeesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    @Autowired
    private EmployeesService employeesService;

    @PostMapping("/add")
    public ResponseEntity<EmployeesDto> createEmployee(@Valid @RequestBody EmployeesDto employeesDto) {
        EmployeesDto savedEmployee = employeesService.addEmployee(employeesDto);
        return ResponseEntity.ok(savedEmployee);
    }
    @PostMapping("/addAll")
    public ResponseEntity<List<EmployeesDto>> addEmployees(
            @Valid @RequestBody EmployeesDtoList employeesList
    ) {
        List<EmployeesDto> saved = employeesService.addAllEmployees(employeesList.getEmployees());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeesDto>> getAllEmployees() {
        List<EmployeesDto> list = employeesService.getAllEmployees();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeesDto> getEmployeeById(@PathVariable Long empId) {
        EmployeesDto dto = employeesService.getEmployeeById(empId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{empId}")
    public ResponseEntity<EmployeesDto> updateEmployee(@PathVariable Long empId, @Valid @RequestBody EmployeesDto updatedDto) {
        EmployeesDto updatedEmployee = employeesService.updateEmployee(empId, updatedDto);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{empId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long empId) {
        employeesService.deleteEmployee(empId);
        return ResponseEntity.noContent().build();
    }
}
