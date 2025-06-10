package com.DM.dairyManagement.service;

import com.DM.dairyManagement.model.Product;
import com.DM.dairyManagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Object getTotalProducts() {
        return productRepository.count();
    }

    public List<Product> getLatestProducts() {
        return productRepository.findTop5ByOrderByIdDesc();
    }

	
}
