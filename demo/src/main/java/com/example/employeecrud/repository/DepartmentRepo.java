package com.example.employeecrud.repository;

import com.example.employeecrud.dao.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, Long> {
}
