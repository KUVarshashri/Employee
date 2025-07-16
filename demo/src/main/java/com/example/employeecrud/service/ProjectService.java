package com.example.employeecrud.service;

import com.example.employeecrud.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto addProject(ProjectDto dto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    void deleteProject(Long id);
    ProjectDto updateProject(Long id, ProjectDto dto);
}
