package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String images;
    private Long roleId;
    private String roleName;
    private boolean enabled;
    private long productCount;
    private LocalDateTime createdAt;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String fullName, String images, Long roleId, String roleName, boolean enabled, long productCount, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.images = images;
        this.roleId = roleId;
        this.roleName = roleName;
        this.enabled = enabled;
        this.productCount = productCount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getProductCount() {
        return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String images;
        private Long roleId;
        private String roleName;
        private boolean enabled;
        private long productCount;
        private LocalDateTime createdAt;

        UserDTOBuilder() {
        }

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDTOBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserDTOBuilder images(String images) {
            this.images = images;
            return this;
        }

        public UserDTOBuilder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserDTOBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public UserDTOBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserDTOBuilder productCount(long productCount) {
            this.productCount = productCount;
            return this;
        }

        public UserDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(id, username, email, fullName, images, roleId, roleName, enabled, productCount, createdAt);
        }
    }
}
