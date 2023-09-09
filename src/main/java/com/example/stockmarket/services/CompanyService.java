package com.example.stockmarket.services;

import com.example.stockmarket.entities.Company;
import com.example.stockmarket.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    // Define methods to interact with the Company entity and repository
    // For example: findAllCompanies(), saveCompany(), etc.
}
