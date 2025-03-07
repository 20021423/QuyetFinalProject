package com.example.ntquyet.controller;

import com.example.ntquyet.model.Product;
import com.example.ntquyet.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String getAllProducts(@RequestParam(value = "success", required = false) String success, Model model) {
        List<Product> products = productService.searchProducts("");
        model.addAttribute("products", products);

        // ✅ Nếu có thông báo đăng nhập thành công, hiển thị trên giao diện
        if ("login".equals(success)) {
            model.addAttribute("successMessage", "Đăng nhập thành công!");
        }

        return "product/product-list";
    }


    // Tìm kiếm sản phẩm
    @GetMapping("/search")
    public String searchProducts(@RequestParam String name, Model model) {
        List<Product> products = productService.searchProducts(name);
        model.addAttribute("products", products);
        return "product/product-list"; // Trả về danh sách sản phẩm với kết quả tìm kiếm
    }

    // Mở form thêm sản phẩm
    @GetMapping("/add")
    public String addProductPage() {
        return "product/add-product"; // Trả về add-product.html
    }

    // Xử lý thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products"; // Sau khi thêm, quay về danh sách sản phẩm
    }
}
