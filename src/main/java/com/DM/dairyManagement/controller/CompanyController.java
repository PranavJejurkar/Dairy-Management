package com.DM.dairyManagement.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.DM.dairyManagement.model.Company;
import com.DM.dairyManagement.repository.CompanyRepository;

import java.util.List;
@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    // Display all companies
    @GetMapping
    public String listCompanies(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        return "companyList";
    }

    // Show form to add a new company
    @GetMapping("/new")
    public String showCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "companyForm";
    }

    // Save a new company
    @PostMapping
    public String saveCompany(@ModelAttribute Company company) {
        companyRepository.save(company);
        return "redirect:/companies";
    }

    // Delete a company
    @PostMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyRepository.deleteById(id);  // Delete the company by its ID
        return "redirect:/companies";      // Redirect to the companies list page
    }
}
