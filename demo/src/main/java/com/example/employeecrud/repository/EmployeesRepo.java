package com.example.employeecrud.repository;

import com.example.employeecrud.dao.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeesRepo extends JpaRepository<Employees, Long> {
    Optional<Employees> findByEmail(String email);

}
