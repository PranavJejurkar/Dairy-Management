package com.DM.dairyManagement.controller;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.DM.dairyManagement.model.Company;
import com.DM.dairyManagement.model.Product;
import com.DM.dairyManagement.model.Unit;
import com.DM.dairyManagement.repository.CompanyRepository;
import com.DM.dairyManagement.repository.ProductRepository;
import com.DM.dairyManagement.repository.UnitRepository;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        List<Unit> units = unitRepository.findAll();
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("units", units);
        model.addAttribute("companies", companies);
        return "productForm";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }
    
    



        // Other methods...

        @PostMapping("/products/{id}/delete")
        public String deleteProduct(@PathVariable("id") Long id) {
            productRepository.deleteById(id);
            return "redirect:/products"; // Redirect back to product list after deletion
        }
    

}
