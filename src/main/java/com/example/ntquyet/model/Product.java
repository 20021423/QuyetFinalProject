package com.example.ntquyet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity lưu thông tin sản phẩm.
 */
@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên sản phẩm
    private String name;

    // Mô tả sản phẩm
    private String description;

    // Giá sản phẩm
    private Double price;

    // Đường dẫn ảnh sản phẩm (URL)
    @Column(name = "image_url")
    private String imageUrl;
}