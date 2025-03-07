package com.example.ntquyet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products", "/products/search", "/css/**").permitAll() // Bất kỳ ai cũng có thể xem sản phẩm
                        .requestMatchers("/cart", "/cart/add").permitAll() // Thêm sản phẩm vào giỏ hàng không cần đăng nhập
                        .requestMatchers("/cart/checkout").authenticated() // Chỉ yêu cầu login khi checkout
                        .requestMatchers("/products/add").hasRole("ADMIN") // Chỉ ADMIN mới có thể thêm sản phẩm
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ✅ Luôn tạo session để giữ trạng thái đăng nhập
                        .sessionFixation().migrateSession() // ✅ Giữ session an toàn
                        .maximumSessions(1).expiredUrl("/auth/login") // ✅ Không cho phép user đăng nhập nhiều lần cùng lúc
                )
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/products", true) // ✅ Sau khi login xong, về danh sách sản phẩm
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/products") // ✅ Đăng xuất xong vẫn vào trang sản phẩm
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(authProvider));
    }
}
