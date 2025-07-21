package com.example.employeecrud.repository;

import com.example.employeecrud.dao.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDetailsRepo extends JpaRepository<BankDetails, Long> {
}
