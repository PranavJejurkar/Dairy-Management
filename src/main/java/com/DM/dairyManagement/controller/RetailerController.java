package com.DM.dairyManagement.controller;

import com.DM.dairyManagement.model.Company;
import com.DM.dairyManagement.model.Retailer;
import com.DM.dairyManagement.repository.CompanyRepository;
import com.DM.dairyManagement.repository.RetailerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/retailers")
public class RetailerController {

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private final String UPLOAD_DIR = "document/";

    @GetMapping("/list")
    public String listRetailers(Model model) {
        List<Retailer> retailers = retailerRepository.findAll();
        model.addAttribute("retailers", retailers);
        return "retailerList";  // Thymeleaf view for listing
    }

    @GetMapping("/add")
    public String showAddRetailerForm(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("retailer", new Retailer());
        model.addAttribute("companies", companies);
        return "addRetailer";  // Thymeleaf view for adding/editing retailer
    }

    @PostMapping("/save")
    public String saveRetailer(@ModelAttribute Retailer retailer,
                               @RequestParam("adharCardPhoto") MultipartFile adharCardPhoto,
                               @RequestParam("passportPhoto") MultipartFile passportPhoto,
                               @RequestParam("licenceCopy") MultipartFile licenceCopy) throws IOException {

        // Save files to the server
        if (!adharCardPhoto.isEmpty()) {
            String adharCardPath = saveFile(adharCardPhoto);
            retailer.setAdharCardPath(adharCardPath);
        }

        if (!passportPhoto.isEmpty()) {
            String passportPhotoPath = saveFile(passportPhoto);
            retailer.setPassportPhotoPath(passportPhotoPath);
        }

        if (!licenceCopy.isEmpty()) {
            String licenceCopyPath = saveFile(licenceCopy);
            retailer.setLicenceCopyPath(licenceCopyPath);
        }

        // Save retailer to database
        retailerRepository.save(retailer);

        return "redirect:/retailers/list";  // Redirect to list of retailers
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Path.of(UPLOAD_DIR + fileName);

        // Ensure directory exists
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
    
    @GetMapping("/edit/{id}")
    public String showEditRetailerForm(@PathVariable("id") Long id, Model model) {
        Retailer retailer = retailerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid retailer ID: " + id));
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("retailer", retailer);
        model.addAttribute("companies", companies);
        return "editRetailer";  // Thymeleaf view for editing retailer
    }

    // Update Retailer
    @PostMapping("/update/{id}")
    public String updateRetailer(@PathVariable("id") Long id,
                                 @ModelAttribute Retailer retailer,
                                 @RequestParam("adharCardPhoto") MultipartFile adharCardPhoto,
                                 @RequestParam("passportPhoto") MultipartFile passportPhoto,
                                 @RequestParam("licenceCopy") MultipartFile licenceCopy) throws IOException {

        Retailer existingRetailer = retailerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid retailer ID: " + id));

        // Update retailer details
        existingRetailer.setName(retailer.getName());
        existingRetailer.setCompany(retailer.getCompany());

        // Save files to the server if present
        if (!adharCardPhoto.isEmpty()) {
            String adharCardPath = saveFile(adharCardPhoto);
            existingRetailer.setAdharCardPath(adharCardPath);
        }
        if (!passportPhoto.isEmpty()) {
            String passportPhotoPath = saveFile(passportPhoto);
            existingRetailer.setPassportPhotoPath(passportPhotoPath);
        }
        if (!licenceCopy.isEmpty()) {
            String licenceCopyPath = saveFile(licenceCopy);
            existingRetailer.setLicenceCopyPath(licenceCopyPath);
        }

        // Save updated retailer to database
        retailerRepository.save(existingRetailer);

        return "redirect:/retailers/list";  // Redirect to list of retailers
    }

    // Delete Retailer
    @GetMapping("/delete/{id}")
    public String deleteRetailer(@PathVariable("id") Long id) {
        retailerRepository.deleteById(id);
        return "redirect:/retailers/list";  // Redirect to list of retailers after deletion
    }
}
