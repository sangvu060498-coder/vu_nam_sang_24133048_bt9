package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyOtpDTO {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Mã OTP không được để trống")
    @Size(min = 6, max = 6, message = "Mã OTP gồm 6 chữ số")
    private String otpCode;

    private String type; // REGISTER, FORGOT_PASSWORD

    public VerifyOtpDTO() {
    }

    public VerifyOtpDTO(String email, String otpCode, String type) {
        this.email = email;
        this.otpCode = otpCode;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static VerifyOtpDTOBuilder builder() {
        return new VerifyOtpDTOBuilder();
    }

    public static class VerifyOtpDTOBuilder {
        private String email;
        private String otpCode;
        private String type;

        VerifyOtpDTOBuilder() {
        }

        public VerifyOtpDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public VerifyOtpDTOBuilder otpCode(String otpCode) {
            this.otpCode = otpCode;
            return this;
        }

        public VerifyOtpDTOBuilder type(String type) {
            this.type = type;
            return this;
        }

        public VerifyOtpDTO build() {
            return new VerifyOtpDTO(email, otpCode, type);
        }
    }
}
