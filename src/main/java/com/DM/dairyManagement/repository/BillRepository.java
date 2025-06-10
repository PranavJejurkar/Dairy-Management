package com.DM.dairyManagement.repository;

import com.DM.dairyManagement.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findTop5ByOrderByIdDesc();
    List<Bill> findByFullName(String fullName);
  
}
