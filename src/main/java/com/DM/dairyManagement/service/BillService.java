package com.DM.dairyManagement.service;

import com.DM.dairyManagement.model.Bill;
import com.DM.dairyManagement.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public long getTotalBills() {
        return billRepository.count();
    }
    
  
    public List<Bill> findBillByCustomerName(String customerName) {
        // Logic to fetch bill by customer name, e.g., using JPA
        return (List<Bill>) billRepository.findByFullName(customerName);
    }
    public List<Bill> getLatestBills() {
        return billRepository.findTop5ByOrderByIdDesc();
    }
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
    public void deleteBillById(Long id) {
        // Check if the bill exists before trying to delete
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            // If the bill exists, delete it
            billRepository.delete(bill.get());
        } else {
            // Handle the case when the bill is not found, you could throw an exception or log it
            throw new IllegalArgumentException("Bill not found with id " + id);
        }
    }
    
    
	
}
