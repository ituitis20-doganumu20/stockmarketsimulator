package com.example.stockmarket.repositories;

import com.example.stockmarket.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
