package com.example.ntquyet.controller;

import com.example.ntquyet.model.OrderItem;
import com.example.ntquyet.model.Product;
import com.example.ntquyet.model.User;
import com.example.ntquyet.service.IOrderService;
import com.example.ntquyet.service.IProductService;
import com.example.ntquyet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CheckoutController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

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
    public String checkout(Authentication authentication) {
        // 1) Kiểm tra đã đăng nhập chưa
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/auth/login?redirect=/cart";
        }

        // 2) principal lúc này là 1 đối tượng org.springframework.security.core.userdetails.User
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // 3) Lấy username từ principal
        String username = springUser.getUsername();

        // 4) Từ username, tìm User (domain) trong DB
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            // Nếu không tìm thấy trong DB, chuyển hướng về login
            return "redirect:/auth/login?redirect=/cart";
        }

        // 5) Đặt hàng
        orderService.placeOrder(user.orElse(null), cart);
        cart.clear();

        // 6) Chuyển hướng về giỏ hàng với thông báo thành công
        return "redirect:/cart?success=checkout";
    }

    // ✅ Tăng số lượng sản phẩm
    @PostMapping("/increase/{productId}")
    @ResponseBody
    public void increaseQuantity(@PathVariable Long productId) {
        for (OrderItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
    }

    // ✅ Giảm số lượng sản phẩm
    @PostMapping("/decrease/{productId}")
    @ResponseBody
    public void decreaseQuantity(@PathVariable Long productId) {
        for (OrderItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    cart.remove(item);
                }
                break;
            }
        }
    }
}
