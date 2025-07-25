package com.example.employeecrud.controller;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dao.Project;
import com.example.employeecrud.dto.EmployeesDto;
import com.example.employeecrud.dto.EmployeesDtoList;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.repository.ProjectRepo;
import com.example.employeecrud.security.JwtUtil;
import com.example.employeecrud.service.EmployeesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private EmployeesRepo emprepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<EmployeesDto> createEmployee(@Valid @RequestBody EmployeesDto employeesDto) {
        EmployeesDto savedEmployee = employeesService.addEmployee(employeesDto);
        return ResponseEntity.ok(savedEmployee);
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<EmployeesDto>> addEmployees(@Valid @RequestBody EmployeesDtoList employeesList) {
        List<EmployeesDto> saved = employeesService.addAllEmployees(employeesList.getEmployees());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeesDto>> getAllEmployees() {
        List<EmployeesDto> list = employeesService.getAllEmployees();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/")
    public ResponseEntity<EmployeesDto> getEmployeeById(HttpServletRequest request) {
        String authHeader= request.getHeader("Authorization");
        String token= authHeader.substring(7);
        long empId=jwtUtil.extractsEmployeesId(token);
        EmployeesDto dto = employeesService.getEmployeeById(empId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<EmployeesDto> updateEmployee(HttpServletRequest request,
                                                       @Valid @RequestBody EmployeesDto updatedDto) {
        String authHeader= request.getHeader("Authorization");
        String token= authHeader.substring(7);
        long empId=jwtUtil.extractsEmployeesId(token);
        EmployeesDto updatedEmployee = employeesService.updateEmployee(empId, updatedDto);
        return updatedEmployee != null ? ResponseEntity.ok(updatedEmployee) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{empId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long empId) {
        employeesService.deleteEmployee(empId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/assign-projects")
    public ResponseEntity<Employees> assignProjectsToEmployee(HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Set<String> projectNames) {
        String authHeader= request.getHeader("Authorization");
        String token= authHeader.substring(7);
        long empId=jwtUtil.extractsEmployeesId(token);
        Optional<Employees> optionalEmployee = employeesRepo.findById(id);
        if (optionalEmployee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
