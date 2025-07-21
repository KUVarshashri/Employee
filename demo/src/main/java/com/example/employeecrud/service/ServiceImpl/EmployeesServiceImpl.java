package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.*;
import com.example.employeecrud.dto.*;
import com.example.employeecrud.repository.*;
import com.example.employeecrud.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesRepo repo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private BankDetailsRepo bankDetailsRepo;

    private EmployeesDto convertToDto(Employees employees) {
        EmployeesDto dto = new EmployeesDto();
        dto.setEmpId(employees.getEmpId());
        dto.setName(employees.getName());
        dto.setEmail(employees.getEmail());
        dto.setPhoneNo(employees.getPhoneNo());
        dto.setPassword(employees.getPassword());

        if (employees.getDepartment() != null) {
            DepartmentDto deptDto = new DepartmentDto();
            deptDto.setDeptId(employees.getDepartment().getDeptId());
            deptDto.setDeptName(employees.getDepartment().getDeptName());
            dto.setDepartment(deptDto);
        }

        if (employees.getIdCard() != null) {
            IDCardDto cardDto = new IDCardDto();
            IDCard idCard = employees.getIdCard();
            cardDto.setCardId(idCard.getCardId());
            cardDto.setCardNumber(idCard.getCardNumber());
            cardDto.setCardType(idCard.getCardType());
            cardDto.setIssueDate(idCard.getIssueDate());
            cardDto.setExpiryDate(idCard.getExpiryDate());
            cardDto.setEmployeeId(employees.getEmpId());
            dto.setIdCard(cardDto);
        }

        if (employees.getBankDetails() != null) {
            BankDetailsDto bankDto = new BankDetailsDto();
            bankDto.setBankId(employees.getBankDetails().getBankId());
            bankDto.setBankName(employees.getBankDetails().getBankName());
            bankDto.setAccountNumber(employees.getBankDetails().getAccountNumber());
            bankDto.setIfscCode(employees.getBankDetails().getIfscCode());
            bankDto.setEmployeeId(employees.getEmpId());
            dto.setBankDetails(bankDto);
        }

        if (employees.getProjects() != null && !employees.getProjects().isEmpty()) {
            Set<ProjectDto> projectDtos = employees.getProjects().stream()
                    .map(project -> {
                        ProjectDto pd = new ProjectDto();
                        pd.setProjectId(project.getProjectId());
                        pd.setProjectName(project.getProjectName());
                        return pd;
                    })
                    .collect(Collectors.toSet());
            dto.setProjects(projectDtos);
        }

        return dto;
    }

    private Employees convertToEntity(EmployeesDto dto) {
        Employees employees = new Employees();
        employees.setEmpId(dto.getEmpId());
        employees.setName(dto.getName());
        employees.setEmail(dto.getEmail());
        employees.setPhoneNo(dto.getPhoneNo());
        employees.setPassword(dto.getPassword());

        if (dto.getDepartment() != null && dto.getDepartment().getDeptId() != null) {
            Department dept = departmentRepo.findById(dto.getDepartment().getDeptId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartment().getDeptId()));
            employees.setDepartment(dept);
        }

        if (dto.getIdCard() != null && dto.getIdCard().getCardNumber() != null) {
            IDCard card = new IDCard();
            card.setCardId(dto.getIdCard().getCardId());
            card.setCardNumber(dto.getIdCard().getCardNumber());
            card.setCardType(dto.getIdCard().getCardType());
            card.setIssueDate(dto.getIdCard().getIssueDate());
            card.setExpiryDate(dto.getIdCard().getExpiryDate());
            card.setEmployee(employees);
            employees.setIdCard(card);
        }

        if (dto.getBankDetails() != null) {
            BankDetails bank = new BankDetails();
            bank.setBankId(dto.getBankDetails().getBankId());
            bank.setBankName(dto.getBankDetails().getBankName());
            bank.setAccountNumber(dto.getBankDetails().getAccountNumber());
            bank.setIfscCode(dto.getBankDetails().getIfscCode());
            bank.setEmployee(employees);
            employees.setBankDetails(bank);
        }

        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {
            Set<Project> projects = dto.getProjects().stream().map(pd -> {
                Project project = projectRepo.findById(pd.getProjectId())
                        .orElseGet(() -> {
                            Project newProject = new Project();
                            newProject.setProjectName(pd.getProjectName());
                            return projectRepo.save(newProject);
                        });
                return project;
            }).collect(Collectors.toSet());
            employees.setProjects(projects);
        }

        return employees;
    }

    @Override
    public EmployeesDto addEmployee(EmployeesDto dto) {
        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        Employees employees = convertToEntity(dto);
        return convertToDto(repo.save(employees));
    }

    @Override
    public List<EmployeesDto> addAllEmployees(List<EmployeesDto> dtoList) {
        return dtoList.stream()
                .map(dto -> {
                    Employees employee = convertToEntity(dto);
                    return convertToDto(repo.save(employee));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeesDto> getAllEmployees() {
        return repo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public EmployeesDto getEmployeeById(Long empId) {
        Employees employees = repo.findById(empId).orElse(null);
        return employees != null ? convertToDto(employees) : null;
    }

    @Override
    public EmployeesDto updateEmployee(Long empId, EmployeesDto dto) {
        Employees existing = repo.findById(empId).orElse(null);
        if (existing != null) {
            Optional<Employees> employeeWithEmail = repo.findByEmail(dto.getEmail());
            if (employeeWithEmail.isPresent() && !employeeWithEmail.get().getEmpId().equals(empId)) {
                throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
            }

            existing.setName(dto.getName());
            existing.setEmail(dto.getEmail());
            existing.setPhoneNo(dto.getPhoneNo());
            existing.setPassword(dto.getPassword());

            if (dto.getDepartment() != null && dto.getDepartment().getDeptId() != null) {
                Department dept = departmentRepo.findById(dto.getDepartment().getDeptId())
                        .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + dto.getDepartment().getDeptId()));
                existing.setDepartment(dept);
            } else {
                existing.setDepartment(null);
            }

            if (dto.getBankDetails() != null) {
                BankDetails bank = existing.getBankDetails() != null ? existing.getBankDetails() : new BankDetails();
                bank.setBankName(dto.getBankDetails().getBankName());
                bank.setAccountNumber(dto.getBankDetails().getAccountNumber());
                bank.setIfscCode(dto.getBankDetails().getIfscCode());
                bank.setEmployee(existing);
                existing.setBankDetails(bank);
            } else {
                existing.setBankDetails(null);
            }

            return convertToDto(repo.save(existing));
        }
        return null;
    }

    @Override
    public void deleteEmployee(Long empId) {
        repo.deleteById(empId);
    }

    @Override
    public EmployeesDto assignProjects(Long empId, Set<String> projectNames) {
        Employees employee = repo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + empId));

        Set<Project> projects = new HashSet<>();

        for (String name : projectNames) {
            Project project = projectRepo.findByProjectName(name);
            if (project == null) {
                project = new Project();
                project.setProjectName(name);
                project = projectRepo.save(project);
            }
            projects.add(project);
        }

        if (employee.getProjects() == null) {
            employee.setProjects(projects);
        } else {
            employee.getProjects().addAll(projects);
        }

        Employees updated = repo.save(employee);
        return convertToDto(updated);
    }
}
