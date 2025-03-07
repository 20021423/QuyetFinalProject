package com.example.ntquyet.service;

import com.example.ntquyet.model.Product;
import java.util.List;

public interface IProductService {
    List<Product> searchProducts(String keyword);
    Product saveProduct(Product product);
}
