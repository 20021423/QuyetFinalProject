package com.example.ntquyet.controller;

import com.example.ntquyet.model.OrderItem;
import com.example.ntquyet.model.Product;
import com.example.ntquyet.model.User;
import com.example.ntquyet.service.IOrderService;
import com.example.ntquyet.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CheckoutController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    private List<OrderItem> cart = new ArrayList<>();

    // ✅ Hiển thị giỏ hàng, truyền đúng `cartItems` vào model
    @GetMapping
    public String viewCart(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("cartItems", cart); // ✅ Đảm bảo `cartItems` có dữ liệu

        // ✅ Nếu có thông báo đặt hàng thành công, hiển thị trên giao diện
        if (success != null) {
            model.addAttribute("successMessage", "Đặt hàng thành công! Cảm ơn bạn đã mua sắm.");
        }

        return "cart/checkout"; // Trả về checkout.html
    }

    // ✅ Thêm sản phẩm vào giỏ hàng, cập nhật `cartItems`
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        Product product = productService.searchProducts("").stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Kiểm tra nếu sản phẩm đã có trong giỏ hàng, chỉ cập nhật số lượng
        for (OrderItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return "redirect:/cart"; // ✅ Tránh thêm sản phẩm trùng lặp
            }
        }

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.add(item);

        return "redirect:/cart"; // ✅ Sau khi thêm vào giỏ hàng, quay về giỏ hàng
    }

    // ✅ Chỉ yêu cầu đăng nhập khi bấm "Check Out"
    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/auth/login?redirect=/cart"; // ✅ Nếu chưa đăng nhập, chuyển đến login và quay lại checkout
        }

        orderService.placeOrder(user, cart);
        cart.clear();
        return "redirect:/cart?success=checkout"; // ✅ Quay lại giỏ hàng với thông báo đặt hàng thành công
    }
}
