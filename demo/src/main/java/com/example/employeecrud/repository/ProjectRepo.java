package com.example.employeecrud.repository;

import com.example.employeecrud.dao.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    Project findByProjectName(String projectName);
}
