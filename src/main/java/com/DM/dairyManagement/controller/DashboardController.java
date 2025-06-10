package com.DM.dairyManagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.DM.dairyManagement.model.Bill;
import com.DM.dairyManagement.model.User;
import com.DM.dairyManagement.repository.BillRepository;
import com.DM.dairyManagement.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/createBill")
    public String showBillForm(Model model) {
        model.addAttribute("bill", new Bill());
        model.addAttribute("products", productRepository.findAll()); // Pass product list
        return "createBill";
    }

    // Save Bill
    @PostMapping("/createBill")
    public String saveBill(@ModelAttribute Bill bill) {
        // Calculate subtotal
        double subtotal = bill.getQty() * bill.getPrice();
        bill.setSubtotal(subtotal);

        // Calculate CGST, SGST, and Discount
        double cgstAmount = (subtotal * bill.getCgst()) / 100;
        double sgstAmount = (subtotal * bill.getSgst()) / 100;
        double discount = bill.getDiscount();

        // Calculate total amount after taxes and discount
        double total = subtotal + cgstAmount + sgstAmount - discount;
        bill.setTotal(total);

        // Calculate balance due
        double balanceDue = total - bill.getPaidAmount();
        bill.setBalanceDue(balanceDue);

        // Save the bill in the database
        billRepository.save(bill);
        return "redirect:/listBill";
    }

    // Show All Bills
    @GetMapping("/listBill")
    public String viewBills(Model model) {
        List<Bill> bills = billRepository.findAll();
        model.addAttribute("bills", bills);
        return "listBill"; // Ensure this matches your HTML file name
    }

    // Delete Bill
//    @PostMapping("/delete-bill/{id}")
//    public String deleteBill(@PathVariable Long id) {
//        billRepository.deleteById(id);
//        return "redirect:/listBill";
//    }

    // Print Bill (Opens a new page for printing)
    @GetMapping("/PrintBill/{id}")
    public String printBill(@PathVariable Long id, Model model) {
        Bill bill = billRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid Bill ID: " + id));
        model.addAttribute("bill", bill);
        return "printBill"; // This should correspond to src/main/resources/templates/printBill.html
    }

    @GetMapping("/next-bill-no")
    @ResponseBody
    public Long getNextBillNo() {
        return billRepository.count() + 1;
    }
}
