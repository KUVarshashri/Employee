package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dao.Project;
import com.example.employeecrud.dto.EmployeesDto;
import com.example.employeecrud.dto.EmployeesDtoList;
import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.repository.ProjectRepo;
import com.example.employeecrud.security.JwtUtil;
import com.example.employeecrud.service.EmployeesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private final String EMPLOYEES_ADDED_SUCCESSFULLY = "Employees added successfully";
    private final String EMPLOYEE_FETCHED_SUCCESSFULLY = "Employee fetched successfully";
    private final String EMPLOYEES_FETCHED_SUCCESSFULLY = "Employees fetched successfully";
    private final String EMPLOYEE_UPDATED_SUCCESSFULLY = "Employee updated successfully";
    private final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private final String EMPLOYEE_UPDATE_FAILED = "Employee not found or update failed";
    private final String EMPLOYEE_DELETED_SUCCESSFULLY = "Employee deleted successfully";
    private final String EMPLOYEE_DELETE_FAILED = "Failed to delete employee: ";
    private final String PROJECTS_ASSIGNED_SUCCESSFULLY = "Projects assigned successfully";

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public GenericResponseEntity<EmployeesDto> createEmployee(@Valid @RequestBody EmployeesDto employeesDto) {
        EmployeesDto savedEmployee = employeesService.addEmployee(employeesDto);
        return GenericResponseEntity.<EmployeesDto>builder()
                .message(EMPLOYEES_ADDED_SUCCESSFULLY)
                .data(savedEmployee)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @PostMapping("/addAll")
    public GenericResponseEntity<List<EmployeesDto>> addEmployees(@Valid @RequestBody EmployeesDtoList employeesList) {
        List<EmployeesDto> saved = employeesService.addAllEmployees(employeesList.getEmployees());
        return GenericResponseEntity.<List<EmployeesDto>>builder()
                .message(EMPLOYEES_ADDED_SUCCESSFULLY)
                .data(saved)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public GenericResponseEntity<List<EmployeesDto>> getAllEmployees() {
        List<EmployeesDto> list = employeesService.getAllEmployees();
        return GenericResponseEntity.<List<EmployeesDto>>builder()
                .message(EMPLOYEES_FETCHED_SUCCESSFULLY)
                .data(list)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @GetMapping("/")
    public GenericResponseEntity<EmployeesDto> getEmployeeById(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        long empId = jwtUtil.extractsEmployeesId(token);
        EmployeesDto dto = employeesService.getEmployeeById(empId);

        if (dto != null) {
            return GenericResponseEntity.<EmployeesDto>builder()
                    .message(EMPLOYEE_FETCHED_SUCCESSFULLY)
                    .data(dto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<EmployeesDto>builder()
                    .message(EMPLOYEE_NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @PutMapping("/update")
    public GenericResponseEntity<EmployeesDto> updateEmployee(HttpServletRequest request,
                                                              @Valid @RequestBody EmployeesDto updatedDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        long empId = jwtUtil.extractsEmployeesId(token);
        EmployeesDto updatedEmployee = employeesService.updateEmployee(empId, updatedDto);

        if (updatedEmployee != null) {
            return GenericResponseEntity.<EmployeesDto>builder()
                    .message(EMPLOYEE_UPDATED_SUCCESSFULLY)
                    .data(updatedEmployee)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<EmployeesDto>builder()
                    .message(EMPLOYEE_UPDATE_FAILED)
                    .data(null)
                    .statusCode(400)
                    .status("BAD_REQUEST")
                    .success(false)
                    .build();
        }
    }

    @DeleteMapping("/delete/{empId}")
    public GenericResponseEntity<Void> deleteEmployee(@PathVariable Long empId) {
        try {
            employeesService.deleteEmployee(empId);
            return GenericResponseEntity.<Void>builder()
                    .message(EMPLOYEE_DELETED_SUCCESSFULLY)
                    .data(null)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return GenericResponseEntity.<Void>builder()
                    .message(EMPLOYEE_DELETE_FAILED + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .status("INTERNAL_SERVER_ERROR")
                    .success(false)
                    .build();
        }
    }

    @PutMapping("/assign-projects")
    public GenericResponseEntity<Employees> assignProjectsToEmployee(HttpServletRequest request,
                                                                     @RequestBody Set<String> projectNames) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        long empId = jwtUtil.extractsEmployeesId(token);

        Optional<Employees> optionalEmployee = employeesRepo.findById(empId);
        if (optionalEmployee.isEmpty()) {
            return GenericResponseEntity.<Employees>builder()
                    .message(EMPLOYEE_NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }

        Employees employee = optionalEmployee.get();
        Set<Project> newProjects = new HashSet<>();

        for (String name : projectNames) {
            Project project = projectRepo.findByProjectName(name);
            if (project == null) {
                project = new Project();
                project.setProjectName(name);
                project = projectRepo.save(project);
            }
            newProjects.add(project);
        }

        employee.setProjects(newProjects);
        Employees updated = employeesRepo.save(employee);

        return GenericResponseEntity.<Employees>builder()
                .message(PROJECTS_ASSIGNED_SUCCESSFULLY)
                .data(updated)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }
}
