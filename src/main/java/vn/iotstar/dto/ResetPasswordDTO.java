package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mã OTP không được để trống")
    private String otpCode;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu mới phải từ 6 ký tự trở lên")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(String email, String otpCode, String newPassword, String confirmPassword) {
        this.email = email;
        this.otpCode = otpCode;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public static ResetPasswordDTOBuilder builder() {
        return new ResetPasswordDTOBuilder();
    }

    public static class ResetPasswordDTOBuilder {
        private String email;
        private String otpCode;
        private String newPassword;
        private String confirmPassword;

        ResetPasswordDTOBuilder() {
        }

        public ResetPasswordDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ResetPasswordDTOBuilder otpCode(String otpCode) {
            this.otpCode = otpCode;
            return this;
        }

        public ResetPasswordDTOBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ResetPasswordDTOBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public ResetPasswordDTO build() {
            return new ResetPasswordDTO(email, otpCode, newPassword, confirmPassword);
        }
    }
}
