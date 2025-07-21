package com.example.employeecrud.service;
import com.example.employeecrud.dto.AddressDto;
import java.util.List;

public interface AddressService {
    AddressDto saveAddress(AddressDto dto);
    List<AddressDto> saveAll(List<AddressDto> addressDtoList); // <-- Add this
    AddressDto getAddressById(Long id);
    List<AddressDto> getAllAddresses();
    AddressDto updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);
}
