package com.example.ntquyet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;

    @Column(nullable = false)
    private String role; // ✅ Đảm bảo role không thể null

    @PrePersist
    public void prePersist() {
        if (this.role == null || this.role.isEmpty()) {
            this.role = "ROLE_USER"; // ✅ Mặc định là ROLE_USER nếu chưa có role
        }
    }
}
