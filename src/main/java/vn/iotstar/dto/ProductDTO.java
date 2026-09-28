package vn.iotstar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private Double price;

    private String description;
    private String imageUrl;
    private Long userId;
    private String userFullName;
    private LocalDateTime createdAt;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price, String description, String imageUrl, Long userId, String userFullName, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.userFullName = userFullName;
        this.createdAt = createdAt;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private Double price;
        private String description;
        private String imageUrl;
        private Long userId;
        private String userFullName;
        private LocalDateTime createdAt;

        ProductDTOBuilder() {
        }

        public ProductDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductDTOBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public ProductDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ProductDTOBuilder userFullName(String userFullName) {
            this.userFullName = userFullName;
            return this;
        }

        public ProductDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(id, name, price, description, imageUrl, userId, userFullName, createdAt);
        }
    }
}
