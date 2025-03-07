package com.example.ntquyet.service.impl;

import com.example.ntquyet.model.Product;
import com.example.ntquyet.repository.ProductRepository;
import com.example.ntquyet.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
