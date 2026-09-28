package vn.iotstar.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String otpCode;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String type; // REGISTER, FORGOT_PASSWORD

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    public OtpToken() {
    }

    public OtpToken(Long id, String otpCode, String email, String type, LocalDateTime expiryDate, boolean used) {
        this.id = id;
        this.otpCode = otpCode;
        this.email = email;
        this.type = type;
        this.expiryDate = expiryDate;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public static OtpTokenBuilder builder() {
        return new OtpTokenBuilder();
    }

    public static class OtpTokenBuilder {
        private Long id;
        private String otpCode;
        private String email;
        private String type;
        private LocalDateTime expiryDate;
        private boolean used = false;

        OtpTokenBuilder() {
        }

        public OtpTokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OtpTokenBuilder otpCode(String otpCode) {
            this.otpCode = otpCode;
            return this;
        }

        public OtpTokenBuilder email(String email) {
            this.email = email;
            return this;
        }

        public OtpTokenBuilder type(String type) {
            this.type = type;
            return this;
        }

        public OtpTokenBuilder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public OtpTokenBuilder used(boolean used) {
            this.used = used;
            return this;
        }

        public OtpToken build() {
            return new OtpToken(id, otpCode, email, type, expiryDate, used);
        }
    }
}
