package com.example.employeecrud.service;

import com.example.employeecrud.dto.IDCardDto;
import java.util.List;

public interface IDCardService {
    IDCardDto saveIDCard(IDCardDto dto);
    IDCardDto getIDCardById(Long id);
    List<IDCardDto> getAllIDCards();
    void deleteIDCard(Long id);
}
