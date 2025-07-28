package com.example.employeecrud.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EmployeesDtoList {

    @NotEmpty(message = "Employee list must not be empty")
    @Valid
    private List<EmployeesDto> employees;
}
