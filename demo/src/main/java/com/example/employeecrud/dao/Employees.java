package com.example.employeecrud.dao;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

        @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
        private BankDetails bankDetails;

        @OneToOne(mappedBy = "employee",cascade = CascadeType.ALL,orphanRemoval = true)
        private IDCard idCard;

        @ManyToOne
        @JoinColumn(name = "department_id", referencedColumnName = "deptId")
        @JsonManagedReference
        private Department department;

        @ManyToMany
        @JoinTable(
                name = "employee_project",
                joinColumns = @JoinColumn(name = "employee_id"),
                inverseJoinColumns = @JoinColumn(name = "project_id")
        )
        private Set<Project> projects;

}
