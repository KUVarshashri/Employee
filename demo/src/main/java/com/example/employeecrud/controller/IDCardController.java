package com.example.employeecrud.controller;

import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.dto.IDCardDto;
import com.example.employeecrud.service.IDCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idcards")
public class IDCardController {

    private static final String CREATED_SUCCESSFULLY = "ID Card created successfully";
    private static final String FOUND = "ID Card found";
    private static final String NOT_FOUND = "ID Card not found";
    private static final String FETCHED_SUCCESSFULLY = "ID Cards fetched successfully";
    private static final String UPDATED_SUCCESSFULLY = "ID Card updated successfully";
    private static final String UPDATE_FAILED = "ID Card not found or update failed";
    private static final String DELETED_SUCCESSFULLY = "ID Card deleted successfully";
    private static final String DELETE_FAILED = "Failed to delete ID Card: ";

    @Autowired
    private IDCardService idCardService;

    @PostMapping
    public GenericResponseEntity<IDCardDto> createIDCard(@RequestBody IDCardDto dto) {
        IDCardDto savedDto = idCardService.saveIDCard(dto);
        return GenericResponseEntity.<IDCardDto>builder()
                .message(CREATED_SUCCESSFULLY)
                .data(savedDto)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public GenericResponseEntity<IDCardDto> getIDCardById(@PathVariable Long id) {
        IDCardDto dto = idCardService.getIDCardById(id);
        if (dto != null) {
            return GenericResponseEntity.<IDCardDto>builder()
                    .message(FOUND)
                    .data(dto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<IDCardDto>builder()
                    .message(NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @GetMapping
    public GenericResponseEntity<List<IDCardDto>> getAllIDCards() {
        List<IDCardDto> list = idCardService.getAllIDCards();
        return GenericResponseEntity.<List<IDCardDto>>builder()
                .message(FETCHED_SUCCESSFULLY)
                .data(list)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @PutMapping("/{id}")
    public GenericResponseEntity<IDCardDto> updateIDCard(@PathVariable Long id, @RequestBody IDCardDto dto) {
        IDCardDto updatedDto = idCardService.updateIDCard(id, dto);
        if (updatedDto != null) {
            return GenericResponseEntity.<IDCardDto>builder()
                    .message(UPDATED_SUCCESSFULLY)
                    .data(updatedDto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<IDCardDto>builder()
                    .message(UPDATE_FAILED)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public GenericResponseEntity<Void> deleteIDCard(@PathVariable Long id) {
        try {
            idCardService.deleteIDCard(id);
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
