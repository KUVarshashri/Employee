package com.example.employeecrud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long deptId;

    @NotBlank(message = "Department name is required")
    private String deptName;
}

