package com.DM.dairyManagement.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.DM.dairyManagement.model.Bill;
import com.DM.dairyManagement.model.Payment;
import com.DM.dairyManagement.repository.BillRepository;
import com.DM.dairyManagement.service.BillService;
import com.DM.dairyManagement.service.PaymentService;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillService billService;
    
    // Show Payment Form
    @GetMapping("/form")
    public String showPaymentForm(Model model) {
        // Fetch all bills from the service or database
        List<Bill> bills = billService.getAllBills();
        
        // Add the bills list to the model
        model.addAttribute("customers", bills);
        model.addAttribute("bills", bills);

        return "payment_form"; // Thymeleaf template for the form
    }
    
    // Fetch Bill details by customer name
    @GetMapping("/billDetails/{customerName}")
    @ResponseBody
    public List<Bill> getBillDetailsByCustomerName(@PathVariable String customerName) {
        return billService.findBillByCustomerName(customerName);
    }

    // Process Payment
    @PostMapping("/add")
    public String processPayment(
            @RequestParam Long billId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false, defaultValue = "0.00") BigDecimal paidAmount,
            @RequestParam(required = false, defaultValue = "0.00") BigDecimal dueAmount,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String creditCardNo,
            @RequestParam(required = false) String upiId,
            Model model) {

        // Set the correct payment details based on the selected payment method
        String finalAccountNo = null;
        String finalCreditCardNo = null;
        String finalUpiId = null;

        if ("Bank Transfer".equals(paymentMethod)) {
            finalAccountNo = accountNo;
        } else if ("Credit Card".equals(paymentMethod)) {
            finalCreditCardNo = creditCardNo;
        } else if ("UPI".equals(paymentMethod)) {
            finalUpiId = upiId;
        }

        // Create and save the payment object
        Payment payment = new Payment(billId, amount, paidAmount, dueAmount, 
                                      paymentMethod, finalAccountNo, finalCreditCardNo, 
                                      finalUpiId, Payment.PaymentStatus.COMPLETED);
        paymentService.savePayment(payment);

        model.addAttribute("message", "Payment successful!");
        return "payment_success";  
    }
    @PostMapping("/add1")
    public String addPayment(@ModelAttribute Payment payment) {
        // Handle payment saving logic
        paymentService.save(payment);
        return "redirect:/payments/list"; // Redirect to list of payments or another page
    }
    // List All Payments
    @GetMapping("/list")
    public String viewPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "payment_list";  // payment_list.html
    }

    // Edit Payment
    @GetMapping("/edit/{id}")
    public String editPayment(@PathVariable("id") Long id, Model model) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            model.addAttribute("payment", payment);
            model.addAttribute("amount", payment.getAmount());
            model.addAttribute("paidAmount", payment.getPaidAmount());
            model.addAttribute("dueAmount", payment.getDueAmount());
        }
        return "editPayment";  // editPayment.html
    }

    // Update Payment
    @PostMapping("/update")
    public String updatePayment(@ModelAttribute Payment payment) {
        paymentService.updatePayment(payment);
        return "redirect:/payments/list"; // Redirect to the list page
    }
}
