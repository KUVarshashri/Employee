package com.example.employeecrud.controller;

import com.example.employeecrud.dto.GenericResponseEntity;
import com.example.employeecrud.dto.ProjectDto;
import com.example.employeecrud.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final String PROJECT_ADDED = "Project added successfully";
    private static final String PROJECT_FOUND = "Project found";
    private static final String PROJECT_NOT_FOUND = "Project not found";
    private static final String PROJECTS_FETCHED = "Projects fetched successfully";
    private static final String PROJECT_UPDATED = "Project updated successfully";
    private static final String UPDATE_FAILED = "Project not found or update failed";
    private static final String DELETE_SUCCESS = "Project deleted successfully";
    private static final String DELETE_FAILED = "Failed to delete project: ";

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public GenericResponseEntity<ProjectDto> addProject(@RequestBody ProjectDto dto) {
        ProjectDto saved = projectService.addProject(dto);
        return GenericResponseEntity.<ProjectDto>builder()
                .message(PROJECT_ADDED)
                .data(saved)
                .statusCode(201)
                .status("CREATED")
                .success(true)
                .build();
    }

    @GetMapping
    public GenericResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return GenericResponseEntity.<List<ProjectDto>>builder()
                .message(PROJECTS_FETCHED)
                .data(projects)
                .statusCode(200)
                .status("OK")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public GenericResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto dto = projectService.getProjectById(id);
        if (dto != null) {
            return GenericResponseEntity.<ProjectDto>builder()
                    .message(PROJECT_FOUND)
                    .data(dto)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<ProjectDto>builder()
                    .message(PROJECT_NOT_FOUND)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @PutMapping("/{id}")
    public GenericResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto dto) {
        ProjectDto updated = projectService.updateProject(id, dto);
        if (updated != null) {
            return GenericResponseEntity.<ProjectDto>builder()
                    .message(PROJECT_UPDATED)
                    .data(updated)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } else {
            return GenericResponseEntity.<ProjectDto>builder()
                    .message(UPDATE_FAILED)
                    .data(null)
                    .statusCode(404)
                    .status("NOT_FOUND")
                    .success(false)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public GenericResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return GenericResponseEntity.<Void>builder()
                    .message(DELETE_SUCCESS)
                    .data(null)
                    .statusCode(200)
                    .status("OK")
                    .success(true)
                    .build();
        } catch (Exception e) {
            return GenericResponseEntity.<Void>builder()
                    .message(DELETE_FAILED + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .status("INTERNAL_SERVER_ERROR")
                    .success(false)
                    .build();
        }
    }
}
