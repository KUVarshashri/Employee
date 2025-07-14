package com.example.employeecrud.controller;

import com.example.employeecrud.dto.IDCardDto;
import com.example.employeecrud.service.IDCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idcards")
public class IDCardController {

    @Autowired
    private IDCardService idCardService;

    @PostMapping
    public ResponseEntity<IDCardDto> createIDCard(@RequestBody IDCardDto dto) {
        IDCardDto savedDto = idCardService.saveIDCard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IDCardDto> getIDCardById(@PathVariable Long id) {
        IDCardDto dto = idCardService.getIDCardById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<IDCardDto>> getAllIDCards() {
        return ResponseEntity.ok(idCardService.getAllIDCards());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIDCard(@PathVariable Long id) {
        idCardService.deleteIDCard(id);
        return ResponseEntity.noContent().build();
    }
}
