package com.example.ntquyet.controller;

import com.example.ntquyet.model.User;
import com.example.ntquyet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder; // <-- Import mới
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute User user,
            @RequestParam(value = "redirect", required = false) String redirectUrl,
            Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            // Lưu thông tin đăng nhập vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("User " + user.getUsername() + " logged in successfully.");

            if (redirectUrl != null) {
                return "redirect:" + redirectUrl;
            }

            return "redirect:/products?success=login";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/auth/login"; // Sau khi đăng ký, chuyển đến login
        } catch (Exception e) {
            model.addAttribute("error", "Username already exists");
            return "auth/register";
        }
    }
}
