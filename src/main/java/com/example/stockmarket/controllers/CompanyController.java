package com.example.stockmarket.controllers;

import com.example.stockmarket.entities.Company;
import com.example.stockmarket.repositories.CompanyRepository;
import com.example.stockmarket.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "companies"; // Return the name of the Thymeleaf template (e.g., "companies.html")
    }
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/getCurrentPrice")
    @ResponseBody // This annotation is used to directly return a response body
    public ResponseEntity<Map<String, Double>> getCurrentPrice() {
        // Fetch the current price from the database
        Company company = companyRepository.findById(1L).orElse(new Company(2000, 1));

        // Create a map to hold the response data
        Map<String, Double> response = new HashMap<>();
        response.put("currentPrice", company.getCurrentPrice());

        return ResponseEntity.ok(response);
    }

    // Define more methods for handling company-related requests


}
