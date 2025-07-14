package com.example.employeecrud.dto;

import com.example.employeecrud.dao.Department;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentDtoList {

    @NotEmpty(message = "Department list must not be empty")
    @Valid
    private List<Department> departments;
}
