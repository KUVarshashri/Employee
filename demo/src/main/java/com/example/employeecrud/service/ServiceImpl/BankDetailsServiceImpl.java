package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.BankDetails;
import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dto.BankDetailsDto;
import com.example.employeecrud.repository.BankDetailsRepo;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.service.BankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankDetailsServiceImpl implements BankDetailsService {

    @Autowired
    private BankDetailsRepo bankDetailsRepo;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Override
    public BankDetailsDto saveBankDetails(BankDetailsDto dto) {
        BankDetails entity = mapToEntity(dto);
        BankDetails saved = bankDetailsRepo.save(entity);
        return mapToDto(saved);
    }

    @Override
    public BankDetailsDto getBankDetailsById(Long id) {
        BankDetails entity = bankDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank details not found with ID: " + id));
        return mapToDto(entity);
    }

    @Override
    public List<BankDetailsDto> getAllBankDetails() {
        return bankDetailsRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BankDetailsDto updateBankDetails(Long id, BankDetailsDto dto) {
        BankDetails existing = bankDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank details not found with ID: " + id));

        existing.setBankName(dto.getBankName());
        existing.setAccountNumber(dto.getAccountNumber());
        existing.setIfscCode(dto.getIfscCode());

        if (dto.getEmployeeId() != null &&
                (existing.getEmployee() == null || !existing.getEmployee().getEmpId().equals(dto.getEmployeeId()))) {

            Employees newEmployee = employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));
            existing.setEmployee(newEmployee);
            newEmployee.setBankDetails(existing);
        }

        BankDetails updated = bankDetailsRepo.save(existing);
        return mapToDto(updated);
    }

    @Override
    public void deleteBankDetails(Long id) {
        BankDetails bankDetails = bankDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank details not found"));
        Employees employee = bankDetails.getEmployee();
        if (employee != null) {
            employee.setBankDetails(null);
            employeesRepo.save(employee);
        }
        bankDetailsRepo.deleteById(id);
    }

    private BankDetailsDto mapToDto(BankDetails entity) {
        BankDetailsDto dto = new BankDetailsDto();
        dto.setBankId(entity.getBankId());
        dto.setBankName(entity.getBankName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setIfscCode(entity.getIfscCode());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmpId());
        }
        return dto;
    }

    private BankDetails mapToEntity(BankDetailsDto dto) {
        BankDetails entity = new BankDetails();
        entity.setBankId(dto.getBankId());
        entity.setBankName(dto.getBankName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setIfscCode(dto.getIfscCode());

        if (dto.getEmployeeId() != null) {
            entity.setEmployee(employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found")));
        }
        return entity;
    }
}
