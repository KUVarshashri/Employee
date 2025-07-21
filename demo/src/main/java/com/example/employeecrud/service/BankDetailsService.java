package com.example.employeecrud.service;

import com.example.employeecrud.dto.BankDetailsDto;
import java.util.List;

public interface BankDetailsService {
    BankDetailsDto saveBankDetails(BankDetailsDto dto);
    BankDetailsDto getBankDetailsById(Long id);
    List<BankDetailsDto> getAllBankDetails();
    BankDetailsDto updateBankDetails(Long id, BankDetailsDto dto);
    void deleteBankDetails(Long id);
}
