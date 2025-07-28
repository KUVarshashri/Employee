package com.example.employeecrud.controller;

import com.example.employeecrud.dto.BankDetailsDto;
import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.service.BankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankdetails")
public class BankDetailsController {

    private static final String CREATED_SUCCESSFULLY = "Bank details created successfully";
    private static final String FOUND = "Bank details found";
    private static final String NOT_FOUND = "Bank details not found";
    private static final String FETCHED_SUCCESSFULLY = "Bank details fetched successfully";
    private static final String UPDATED_SUCCESSFULLY = "Bank details updated successfully";
    private static final String UPDATE_FAILED = "Bank details not found or update failed";
    private static final String DELETED_SUCCESSFULLY = "Bank details deleted successfully";
    private static final String DELETE_FAILED = "Failed to delete bank details: ";

    @Autowired
    private BankDetailsService bankDetailsService;

    @PostMapping
    public GenericResponseEntity<BankDetailsDto> create(@RequestBody BankDetailsDto dto) {
        BankDetailsDto saved = bankDetailsService.saveBankDetails(dto);
        return GenericResponseEntity.<BankDetailsDto>builder()
                .message(CREATED_SUCCESSFULLY)
                .data(saved)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public GenericResponseEntity<BankDetailsDto> getById(@PathVariable Long id) {
        BankDetailsDto dto = bankDetailsService.getBankDetailsById(id);
        if (dto != null) {
            return GenericResponseEntity.<BankDetailsDto>builder()
                    .message(FOUND)
                    .data(dto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<BankDetailsDto>builder()
                    .message(NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @GetMapping
    public GenericResponseEntity<List<BankDetailsDto>> getAll() {
        List<BankDetailsDto> list = bankDetailsService.getAllBankDetails();
        return GenericResponseEntity.<List<BankDetailsDto>>builder()
                .message(FETCHED_SUCCESSFULLY)
                .data(list)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public GenericResponseEntity<BankDetailsDto> update(@PathVariable Long id, @RequestBody BankDetailsDto dto) {
        BankDetailsDto updated = bankDetailsService.updateBankDetails(id, dto);
        if (updated != null) {
            return GenericResponseEntity.<BankDetailsDto>builder()
                    .message(UPDATED_SUCCESSFULLY)
                    .data(updated)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<BankDetailsDto>builder()
                    .message(UPDATE_FAILED)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public GenericResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            bankDetailsService.deleteBankDetails(id);
            return GenericResponseEntity.<Void>builder()
                    .message(DELETED_SUCCESSFULLY)
                    .data(null)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return GenericResponseEntity.<Void>builder()
                    .message(DELETE_FAILED + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .status("INTERNAL_SERVER_ERROR")
                    .success(false)
                    .build();
        }
    }
}
