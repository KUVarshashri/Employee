package com.example.employeecrud.controller;

import com.example.employeecrud.dto.AddressDto;
import com.example.employeecrud.dto.AddressDtoList;
import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final String ADDRESS_ADDED_SUCCESSFULLY = "Address added successfully";
    private final String ADDRESSES_ADDED_SUCCESSFULLY = "Addresses added successfully";
    private final String ADDRESSES_FETCHED_SUCCESSFULLY = "Addresses fetched successfully";
    private final String ADDRESS_FOUND = "Address found";
    private final String ADDRESS_NOT_FOUND = "Address not found";
    private final String ADDRESS_UPDATED_SUCCESSFULLY = "Address updated successfully";
    private final String ADDRESS_UPDATE_FAILED = "Address not found or update failed";
    private final String ADDRESS_DELETED_SUCCESSFULLY = "Address deleted successfully";
    private final String ADDRESS_DELETE_FAILED = "Failed to delete address: ";

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public GenericResponseEntity<AddressDto> addAddress(@RequestBody AddressDto dto) {
        AddressDto saved = addressService.saveAddress(dto);
        return GenericResponseEntity.<AddressDto>builder()
                .message(ADDRESS_ADDED_SUCCESSFULLY)
                .data(saved)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @PostMapping("/addAll")
    public GenericResponseEntity<List<AddressDto>> addAllAddresses(@RequestBody AddressDtoList dtoList) {
        List<AddressDto> savedList = addressService.saveAll(dtoList.getAddresses());
        return GenericResponseEntity.<List<AddressDto>>builder()
                .message(ADDRESSES_ADDED_SUCCESSFULLY)
                .data(savedList)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public GenericResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addresses = addressService.getAllAddresses();
        return GenericResponseEntity.<List<AddressDto>>builder()
                .message(ADDRESSES_FETCHED_SUCCESSFULLY)
                .data(addresses)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public GenericResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        AddressDto dto = addressService.getAddressById(id);
        if (dto != null) {
            return GenericResponseEntity.<AddressDto>builder()
                    .message(ADDRESS_FOUND)
                    .data(dto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<AddressDto>builder()
                    .message(ADDRESS_NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @PutMapping("/update/{id}")
    public GenericResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto dto) {
        AddressDto updated = addressService.updateAddress(id, dto);
        if (updated != null) {
            return GenericResponseEntity.<AddressDto>builder()
                    .message(ADDRESS_UPDATED_SUCCESSFULLY)
                    .data(updated)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<AddressDto>builder()
                    .message(ADDRESS_UPDATE_FAILED)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public GenericResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return GenericResponseEntity.<Void>builder()
                    .message(ADDRESS_DELETED_SUCCESSFULLY)
                    .data(null)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return GenericResponseEntity.<Void>builder()
                    .message(ADDRESS_DELETE_FAILED + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .status("INTERNAL_SERVER_ERROR")
                    .success(false)
                    .build();
        }
    }
}
