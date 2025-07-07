package com.example.employeecrud.repository;
import com.example.employeecrud.dao.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployeesRepo extends JpaRepository<Employees, Long> {
    boolean existsByEmail(String email);
}
