package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dto.IDCardDto;
import com.example.employeecrud.dao.IDCard;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.repository.IDCardRepo;
import com.example.employeecrud.service.IDCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IDCardServiceImpl implements IDCardService {

    @Autowired
    private IDCardRepo idCardRepository;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Override
    public IDCardDto saveIDCard(IDCardDto dto) {
        IDCard idCard = mapToEntity(dto);
        IDCard saved = idCardRepository.save(idCard);
        return mapToDto(saved);
    }

    @Override
    public IDCardDto getIDCardById(Long id) {
        IDCard card = idCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID Card not found"));
        return mapToDto(card);
    }

    @Override
    public List<IDCardDto> getAllIDCards() {
        return idCardRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteIDCard(Long id) {
        Employees employee = idCardRepository.findById(id).get().getEmployee();
        if (employee != null) {
            employee.setIdCard(null);
            employeesRepo.save(employee);
        }
        idCardRepository.deleteById(id);
    }

    @Override
    public IDCardDto updateIDCard(Long id, IDCardDto dto) {
        IDCard existing = idCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID Card not found with ID: " + id));

        existing.setCardNumber(dto.getCardNumber());
        existing.setCardType(dto.getCardType());
        existing.setIssueDate(dto.getIssueDate());
        existing.setExpiryDate(dto.getExpiryDate());

        if (dto.getEmployeeId() != null) {
            Employees employee = employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));
            existing.setEmployee(employee);
        }

        IDCard updated = idCardRepository.save(existing);
        return mapToDto(updated);
    }

    private IDCardDto mapToDto(IDCard entity) {
        IDCardDto dto = new IDCardDto();
        dto.setCardId(entity.getCardId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCardType(entity.getCardType());
        dto.setIssueDate(entity.getIssueDate());
        dto.setExpiryDate(entity.getExpiryDate());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmpId());
        }
        return dto;
    }

    private IDCard mapToEntity(IDCardDto dto) {
        IDCard entity = new IDCard();
        entity.setCardId(dto.getCardId());
        entity.setCardNumber(dto.getCardNumber());
        entity.setCardType(dto.getCardType());
        entity.setIssueDate(dto.getIssueDate());
        entity.setExpiryDate(dto.getExpiryDate());

        if (dto.getEmployeeId() != null) {
            entity.setEmployee(employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId())));
        }

        return entity;
    }
}
