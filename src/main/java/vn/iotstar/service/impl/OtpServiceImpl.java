package vn.iotstar.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.repository.OtpTokenRepository;
import vn.iotstar.service.EmailService;
import vn.iotstar.service.OtpService;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int OTP_MINUTES = 5;

    private final OtpTokenRepository otpTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public OtpServiceImpl(OtpTokenRepository otpTokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.otpTokenRepository = otpTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    private void send(String email, String type, String subject) {
        try {
            otpTokenRepository.deleteByEmailAndType(email, type);
        } catch (Exception ignored) {
        }

        String otp = generateOtp();
        OtpToken token = OtpToken.builder()
                .email(email)
                .otpHash(passwordEncoder.encode(otp))
                .type(type)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_MINUTES))
                .attempts(0)
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpTokenRepository.save(token);
        emailService.sendOtpEmail(email, otp, subject);
    }

    @Override
    @Transactional
    public String generateAndSaveOtp(String email, String type) {
        send(email, type, "Xác thực mã OTP");
        return "OK";
    }

    @Override
    @Transactional
    public boolean validateOtp(String email, String otpCode, String type) {
        OtpToken token = otpTokenRepository
                .findTopByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(email, type)
                .orElse(null);

        if (token == null || token.getExpiresAt().isBefore(LocalDateTime.now()) || token.getAttempts() >= MAX_ATTEMPTS) {
            return false;
        }

        token.setAttempts(token.getAttempts() + 1);
        if (!passwordEncoder.matches(otpCode, token.getOtpHash())) {
            otpTokenRepository.save(token);
            return false;
        }

        token.setUsed(true);
        otpTokenRepository.save(token);
        return true;
    }
}
