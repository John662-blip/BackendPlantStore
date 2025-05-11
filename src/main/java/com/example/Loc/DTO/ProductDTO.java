package com.example.Loc.DTO;

import com.example.Loc.Modal.Product;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String imageUrl;
    private String productName;
    private Long categoryId;
    private Long stockCount;
    private Long soldCount;
    private String description;
    private float humadity;
    private int temperature;
    private Long price;
    private String createDate;
    private Short status;
    public static ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setImageUrl(product.getImageUrl());
        dto.setProductName(product.getProductName());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setStockCount(product.getStockCount());
        dto.setSoldCount(product.getSoldCount());
        dto.setDescription(product.getDescription());
        dto.setHumadity(product.getHumadity());
        dto.setTemperature(product.getTemperature());
        dto.setPrice(product.getPrice());
        dto.setCreateDate(product.getCreateDate().toString());
        dto.setStatus(product.getStatus());
        return dto;
    }
}