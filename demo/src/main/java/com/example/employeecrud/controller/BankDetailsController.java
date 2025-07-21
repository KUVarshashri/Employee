package com.example.employeecrud.controller;

import com.example.employeecrud.dto.BankDetailsDto;
import com.example.employeecrud.service.BankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankdetails")
public class BankDetailsController {

    @Autowired
    private BankDetailsService bankDetailsService;

    @PostMapping
    public ResponseEntity<BankDetailsDto> create(@RequestBody BankDetailsDto dto) {
        BankDetailsDto saved = bankDetailsService.saveBankDetails(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDetailsDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bankDetailsService.getBankDetailsById(id));
    }

    @GetMapping
    public ResponseEntity<List<BankDetailsDto>> getAll() {
        return ResponseEntity.ok(bankDetailsService.getAllBankDetails());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankDetailsDto> update(@PathVariable Long id, @RequestBody BankDetailsDto dto) {
        BankDetailsDto updated = bankDetailsService.updateBankDetails(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankDetailsService.deleteBankDetails(id);
        return ResponseEntity.noContent().build();
    }
}
