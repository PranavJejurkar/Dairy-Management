package com.DM.dairyManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DM.dairyManagement.model.Payment;
import com.DM.dairyManagement.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByBillId(Long billId) {
        return paymentRepository.findByBillId(billId);
    }

    // ✅ Fix: Renamed to getLatestPayments() and used paymentDate for ordering
    public List<Payment> getLatestPayments() {
        return paymentRepository.findTop3ByOrderByPaymentDateDesc();
    }

    public long getTotalPayments() {
        return paymentRepository.count();
    }

    public long getCompletedTransactionCount() {
        return paymentRepository.countByDueAmount(0);
    }

    public long getIncompleteTransactionCount() {
        return paymentRepository.countByDueAmountGreaterThan(0);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

	public Object getLatestTransactions() {
		// TODO Auto-generated method stub
		return paymentRepository.findTop3ByOrderByPaymentDateDesc();
	}
	public Payment getPaymentById(Long id) {
	    return paymentRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Payment not found"));
	}

	public void updatePayment(Payment payment) {
	    Payment existingPayment = paymentRepository.findById(payment.getId()).orElse(null);
	    
	    if (existingPayment != null) {
	        existingPayment.setPaidAmount(payment.getPaidAmount());
	        existingPayment.setDueAmount(payment.getAmount().subtract(payment.getPaidAmount()));
	        existingPayment.setPaymentMethod(payment.getPaymentMethod());
	        existingPayment.setPaymentDate(payment.getPaymentDate());
	        
	        // Save updated payment details
	        paymentRepository.save(existingPayment);
	    }
	}

	public void save(Payment payment) {
		// TODO Auto-generated method stub
		
	}


}