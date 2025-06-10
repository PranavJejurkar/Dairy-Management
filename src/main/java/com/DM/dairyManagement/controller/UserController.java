package com.DM.dairyManagement.controller;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.DM.dairyManagement.model.User;
import com.DM.dairyManagement.service.BillService;
import com.DM.dairyManagement.service.CompanyService;
import com.DM.dairyManagement.service.PaymentService;
import com.DM.dairyManagement.service.ProductService;
import com.DM.dairyManagement.service.RetailerService;
import com.DM.dairyManagement.service.UnitService;
import com.DM.dairyManagement.service.UserService;
import com.DM.dairyManagement.service.WholesalerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

  
    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;


    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RetailerService retailerService;

    @Autowired
    private WholesalerService wholesalerService;

    @Autowired
    private UnitService unitService;


    // Register Page
    @GetMapping("/signup")
    public String showRegisterPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(User user, @RequestParam("photo") MultipartFile photo) throws IOException {
        if (!photo.isEmpty()) {
            String fileName = StringUtils.cleanPath(photo.getOriginalFilename());

            // Define upload directory outside resources
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            File file = new File(uploadDir + fileName);
            photo.transferTo(file);

            // Store only the relative path
            user.setPhotoPath("/uploads/" + fileName);
        }

        userService.saveUser(user);
        return "redirect:/login";
    }



    // Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Authenticate User
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, 
                            @RequestParam("password") String password, 
                            Model model, HttpSession session) {
        User user = userService.authenticateUser(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }


    // Dashboard Page
//    @GetMapping("/dashboard")
//    public String dashboard(Model model, HttpSession session) {
//        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        model.addAttribute("user", user);
//        model.addAttribute("totalCompanies", companyService.getTotalCompanies());
//        model.addAttribute("totalProducts", productService.getTotalProducts()); // FIXED
//        model.addAttribute("totalBills", billService.getTotalBills());
//        model.addAttribute("totalPayments", paymentService.getTotalPayments());
//        model.addAttribute("totalRetailers", retailerService.getTotalRetailers());
//        model.addAttribute("totalWholesalers", wholesalerService.getTotalWholesalers());
//        model.addAttribute("totalUnits", unitService.getTotalUnits());
//
//        // Fetch latest lists
//        model.addAttribute("latestCompanies", companyService.getLatestCompanies());
//        model.addAttribute("latestProducts", productService.getLatestProducts());
//        model.addAttribute("latestBills", billService.getLatestBills());
//        model.addAttribute("latestPayments", paymentService.getLatestPayments());
//
//        return "dashboard";
//    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("totalCompanies", companyService.getTotalCompanies());
        model.addAttribute("totalProducts", productService.getTotalProducts());
        model.addAttribute("totalBills", billService.getTotalBills());
        model.addAttribute("totalPayments", paymentService.getTotalPayments());
        model.addAttribute("totalRetailers", retailerService.getTotalRetailers());
        model.addAttribute("totalWholesalers", wholesalerService.getTotalWholesalers());
        model.addAttribute("totalUnits", unitService.getTotalUnits());

        // Fetch latest lists
        model.addAttribute("latestCompanies", companyService.getLatestCompanies());
        model.addAttribute("latestProducts", productService.getLatestProducts());
        model.addAttribute("latestBills", billService.getLatestBills());
        model.addAttribute("latestPayments", paymentService.getLatestPayments());

        // Fetch recent transactions (latest 5 transactions)
        model.addAttribute("latestTransactions", paymentService.getLatestTransactions());

        // Fetch transaction count for chart (Complete & Incomplete)
        model.addAttribute("completedTransactions", paymentService.getCompletedTransactionCount());
        model.addAttribute("incompleteTransactions", paymentService.getIncompleteTransactionCount());

        return "dashboard";
    }

    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
 // Show Edit Profile Page
    @GetMapping("/edit-profile")
    public String showEditProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/edit-profile";
        }
        model.addAttribute("user", user);
        return "edit-profile";
    }

    // Handle Edit Profile Form Submission
    @PostMapping("/edit-profile")
    public String editProfile(User user, @RequestParam("photo") MultipartFile photo, HttpSession session) throws IOException {
        // Check if a new photo was uploaded
        if (!photo.isEmpty()) {
            String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            File file = new File(uploadDir + fileName);
            photo.transferTo(file);

            // Store only the relative path
            user.setPhotoPath("/uploads/" + fileName);
        }

        userService.updateUser(user);
        session.setAttribute("loggedInUser", user); // Update session with new data
        return "redirect:/dashboard";
    }


}
