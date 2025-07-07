package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dto.EmployeesDto;
import com.example.employeecrud.exception.EmailAlreadyExistsException;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesRepo repo;

    private EmployeesDto convertToDto(Employees employees) {
        EmployeesDto dto = new EmployeesDto();
        dto.setEmpId(employees.getEmpId());
        dto.setName(employees.getName());
        dto.setEmail(employees.getEmail());
        dto.setPhoneNo(employees.getPhoneNo());
        dto.setPassword(employees.getPassword());
        return dto;
    }

    private Employees convertToEntity(EmployeesDto dto) {
        Employees employees = new Employees();
        employees.setEmpId(dto.getEmpId());
        employees.setName(dto.getName());
        employees.setEmail(dto.getEmail());
        employees.setPhoneNo(dto.getPhoneNo());
        employees.setPassword(dto.getPassword());
        return employees;
    }

    @Override
    public EmployeesDto addEmployee(EmployeesDto dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email '" + dto.getEmail() + "' is already registered.");
        }
        Employees employees = convertToEntity(dto);
        return convertToDto(repo.save(employees));
    }

    @Override
    public List<EmployeesDto> addAllEmployees(List<EmployeesDto> dtoList) {
        return dtoList.stream().map(dto -> {
            if (repo.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Email '" + dto.getEmail() + "' is already registered.");
            }
            Employees employee = convertToEntity(dto);
            return convertToDto(repo.save(employee));
        }).collect(Collectors.toList());
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
            if (!existing.getEmail().equals(dto.getEmail()) && repo.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Email '" + dto.getEmail() + "' is already registered.");
            }
            existing.setName(dto.getName());
            existing.setEmail(dto.getEmail());
            existing.setPhoneNo(dto.getPhoneNo());
            existing.setPassword(dto.getPassword());
            return convertToDto(repo.save(existing));
        }
        return null;
    }

    @Override
    public void deleteEmployee(Long empId) {
        repo.deleteById(empId);
    }
}
