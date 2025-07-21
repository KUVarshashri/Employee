package com.example.employeecrud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDto {

    private Long bankId;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private Long employeeId;
}
