package com.example.demo.dto;
import com.example.demo.entities.Product;

public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String category;
    private String imageUrl;



    public ProductDTO() {}

     // Convert Entity to DTO
    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;
        
        ProductDTO dto = new ProductDTO();
        dto.setId((long) product.getProductId());
        dto.setName(product.getProductName());
        dto.setPrice(product.getProductPrice());
        dto.setDescription(product.getProductDescription());
        return dto;
    }

    // Convert DTO to Entity
    public Product toEntity() {
        Product product = new Product();
        if (this.id != null) {
            product.setProductId(this.id.intValue());
        }
        product.setProductName(this.name);
        product.setProductPrice(this.price);
        product.setProductDescription(this.description);
        return product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
