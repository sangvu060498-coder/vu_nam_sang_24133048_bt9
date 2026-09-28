package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    public RegisterDTO() {
    }

    public RegisterDTO(String username, String email, String password, String confirmPassword, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static RegisterDTOBuilder builder() {
        return new RegisterDTOBuilder();
    }

    public static class RegisterDTOBuilder {
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
        private String fullName;

        RegisterDTOBuilder() {
        }

        public RegisterDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public RegisterDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterDTOBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public RegisterDTOBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public RegisterDTO build() {
            return new RegisterDTO(username, email, password, confirmPassword, fullName);
        }
    }
}
