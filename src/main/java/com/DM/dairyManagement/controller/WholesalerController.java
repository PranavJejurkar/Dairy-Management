package com.DM.dairyManagement.controller;

import com.DM.dairyManagement.model.Company;
import com.DM.dairyManagement.model.Wholesaler;
import com.DM.dairyManagement.repository.CompanyRepository;
import com.DM.dairyManagement.repository.WholesalerRepository;
import com.DM.dairyManagement.service.WholesalerService;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/wholesalers")
public class WholesalerController {

    @Autowired
    private WholesalerRepository wholesalerRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private WholesalerService wholesalerService;

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/list")
    public String listWholesalers(Model model) {
        List<Wholesaler> wholesalers = wholesalerRepository.findAll();
        model.addAttribute("wholesalers", wholesalers);
        return "wholesalerList";  // Thymeleaf view
    }

    @GetMapping("/add")
    public String showAddWholesalerForm(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("wholesaler", new Wholesaler());
        model.addAttribute("companies", companies);
        return "addWholesaler";  
    }

    @PostMapping("/save")
    public String saveWholesaler(@ModelAttribute Wholesaler wholesaler,
                                 @RequestParam(value = "adharCardPhoto", required = false) MultipartFile adharCardPhoto,
                                 @RequestParam(value = "passportPhoto", required = false) MultipartFile passportPhoto,
                                 @RequestParam(value = "licenceCopy", required = false) MultipartFile licenceCopy) throws IOException {

        if (adharCardPhoto != null && !adharCardPhoto.isEmpty()) {
            wholesaler.setAdharCardPath(saveFile(adharCardPhoto));
        }

        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            wholesaler.setPassportPhotoPath(saveFile(passportPhoto));
        }

        if (licenceCopy != null && !licenceCopy.isEmpty()) {
            wholesaler.setLicenceCopyPath(saveFile(licenceCopy));
        }

        wholesalerRepository.save(wholesaler);
        return "redirect:/wholesalers/list";
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";  // Return empty string instead of null
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Path.of(UPLOAD_DIR, fileName);

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();  // Create directory if it does not exist
        }

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    @GetMapping("/edit/{id}")
    public String showEditWholesalerForm(@PathVariable("id") Long id, Model model) {
        Wholesaler wholesaler = wholesalerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wholesaler ID: " + id));

        List<Company> companies = companyRepository.findAll();
        model.addAttribute("wholesaler", wholesaler);
        model.addAttribute("companies", companies);
        return "editWholesaler";
    }

    @PostMapping("/update/{id}")
    public String updateWholesaler(@PathVariable("id") Long id,
                                   @ModelAttribute Wholesaler wholesaler,
                                   @RequestParam(value = "adharCardPhoto", required = false) MultipartFile adharCardPhoto,
                                   @RequestParam(value = "passportPhoto", required = false) MultipartFile passportPhoto,
                                   @RequestParam(value = "licenceCopy", required = false) MultipartFile licenceCopy) throws IOException {

        Wholesaler existingWholesaler = wholesalerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wholesaler ID: " + id));

        existingWholesaler.setName(wholesaler.getName());
        existingWholesaler.setCompany(wholesaler.getCompany());

        if (adharCardPhoto != null && !adharCardPhoto.isEmpty()) {
            existingWholesaler.setAdharCardPath(saveFile(adharCardPhoto));
        }

        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            existingWholesaler.setPassportPhotoPath(saveFile(passportPhoto));
        }

        if (licenceCopy != null && !licenceCopy.isEmpty()) {
            existingWholesaler.setLicenceCopyPath(saveFile(licenceCopy));
        }

        wholesalerRepository.save(existingWholesaler);
        return "redirect:/wholesalers/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteWholesaler(@PathVariable("id") Long id) {
        wholesalerRepository.deleteById(id);
        return "redirect:/wholesalers/list";
    }

    
    @GetMapping("/count")
    public ResponseEntity<Long> getWholesalerCount() {
        long count = wholesalerService.getTotalWholesalers();
        return ResponseEntity.ok(count);
    }
}
