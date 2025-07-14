package com.example.employeecrud.dao;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IDCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    @Column(nullable = false,unique = true)
    private String cardNumber;

    @OneToOne
    @JoinColumn(name = "employee_id",referencedColumnName = "empId")
    private Employees employee;
}

