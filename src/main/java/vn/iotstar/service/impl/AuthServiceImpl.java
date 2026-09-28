package vn.iotstar.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.ForgotPasswordDTO;
import vn.iotstar.dto.RegisterDTO;
import vn.iotstar.dto.ResetPasswordDTO;
import vn.iotstar.dto.VerifyOtpDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.AuthService;
import vn.iotstar.service.EmailService;
import vn.iotstar.service.OtpService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new RuntimeException("Username đã được sử dụng.");
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký.");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        Role userRole = roleRepository.findByNameIgnoreCase("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .images("/images/avatar-default.png")
                .enabled(false) // Cần xác thực OTP để kích hoạt
                .role(userRole)
                .build();

        userRepository.save(user);

        // Sinh mã OTP và gửi qua Email
        String otpCode = otpService.generateAndSaveOtp(dto.getEmail(), "REGISTER");
        emailService.sendOtpEmail(dto.getEmail(), otpCode, "Mã OTP Xác thực Đăng ký Tài khoản");
    }

    @Override
    @Transactional
    public boolean verifyRegistrationOtp(VerifyOtpDTO dto) {
        boolean isValid = otpService.validateOtp(dto.getEmail(), dto.getOtpCode(), "REGISTER");
        if (isValid) {
            User user = userRepository.findByEmailIgnoreCase(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void processForgotPassword(ForgotPasswordDTO dto) {
        User user = userRepository.findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email chưa được đăng ký trong hệ thống."));

        String otpCode = otpService.generateAndSaveOtp(user.getEmail(), "FORGOT_PASSWORD");
        emailService.sendOtpEmail(user.getEmail(), otpCode, "Mã OTP Đặt lại Mật khẩu");
    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        boolean isValid = otpService.validateOtp(dto.getEmail(), dto.getOtpCode(), "FORGOT_PASSWORD");
        if (isValid) {
            User user = userRepository.findByEmailIgnoreCase(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
