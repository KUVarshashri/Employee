package com.example.employeecrud.service.ServiceImpl;

import com.example.employeecrud.dao.Employees;
import com.example.employeecrud.dao.Project;
import com.example.employeecrud.dto.ProjectDto;
import com.example.employeecrud.repository.EmployeesRepo;
import com.example.employeecrud.repository.ProjectRepo;
import com.example.employeecrud.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private EmployeesRepo employeesRepo;

    private ProjectDto convertToDto(Project entity) {
        return new ProjectDto(entity.getProjectId(), entity.getProjectName());
    }

    private Project convertToEntity(ProjectDto dto) {
        Project entity = new Project();
        entity.setProjectId(dto.getProjectId());
        entity.setProjectName(dto.getProjectName());
        return entity;
    }

    @Override
    public ProjectDto addProject(ProjectDto dto) {
        Project project = convertToEntity(dto);
        return convertToDto(projectRepo.save(project));
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return projectRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDto(project);
    }

    @Override
    public void deleteProject(Long id) {
        Project project=projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        Set<Employees> employeesSet=project.getEmployees();
        for(Employees employee:employeesSet){
            employee.getProjects().remove(project);
            employeesRepo.save(employee);
        }
        projectRepo.deleteById(id);
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto dto) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setProjectName(dto.getProjectName());
        return convertToDto(projectRepo.save(project));
    }
}
