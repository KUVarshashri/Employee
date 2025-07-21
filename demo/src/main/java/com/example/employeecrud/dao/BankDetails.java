package com.example.employeecrud.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    private String ifscCode;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "empId")
    @JsonIgnore
    private Employees employee;
}