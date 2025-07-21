package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.Address;
import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dto.AddressDto;
import com.example.employeecrud.repository.AddressRepo;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Override
    @Transactional
    public AddressDto saveAddress(AddressDto dto) {
        Address entity = mapToEntity(dto);
        Address saved = addressRepo.save(entity);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public List<AddressDto> saveAll(List<AddressDto> addressDtoList) {
        List<Address> entities = addressDtoList.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        List<Address> saved = addressRepo.saveAll(entities);
        return saved.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressById(Long id) {
        Address address = addressRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));
        return mapToDto(address);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        return addressRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long id, AddressDto dto) {
        Address address = addressRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());

        if (dto.getEmployeeId() != null) {
            Employees emp = employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));
            address.setEmployee(emp);
        }

        Address updated = addressRepo.save(address);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        if (!addressRepo.existsById(id)) {
            throw new RuntimeException("Address not found with ID: " + id);
        }
        addressRepo.deleteById(id);
    }

    private Address mapToEntity(AddressDto dto) {
        Address address = new Address();
        address.setAddressId(dto.getId());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());

        if (dto.getEmployeeId() != null) {
            Employees emp = employeesRepo.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));
            address.setEmployee(emp);
        }

        return address;
    }

    private AddressDto mapToDto(Address entity) {
        AddressDto dto = new AddressDto();
        dto.setId(entity.getAddressId());
        dto.setStreet(entity.getStreet());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPostalCode(entity.getPostalCode());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmpId());
        }

        return dto;
    }
}
