package com.example.employeecrud.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDtoList {
    private List<ProjectDto> projects;
}
