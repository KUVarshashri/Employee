package com.example.employeecrud.controller;

import com.example.employeecrud.dto.AddressDto;
import com.example.employeecrud.dto.AddressDtoList;
import com.example.employeecrud.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto dto) {
        return ResponseEntity.ok(addressService.saveAddress(dto));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<AddressDto>> addAllAddresses(@RequestBody AddressDtoList dtoList) {
        return ResponseEntity.ok(addressService.saveAll(dtoList.getAddresses()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto dto) {
        return ResponseEntity.ok(addressService.updateAddress(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
