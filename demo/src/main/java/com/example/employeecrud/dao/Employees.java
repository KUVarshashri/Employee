package com.example.employeecrud.dao;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employees {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long empId;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false, unique = true)
        private String email;

        private String phoneNo;

        @Column(nullable = false)
        private String password;
    }
