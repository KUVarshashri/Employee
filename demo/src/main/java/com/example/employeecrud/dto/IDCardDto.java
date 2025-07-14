package com.example.employeecrud.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IDCardDto {

    private Long cardId;
    private String cardNumber;
    private Long employeeId;
}
