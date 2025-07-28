package com.example.employeecrud.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDtoList {
    private List<ProjectDto> projects;
}
