package vn.iotstar.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.repository.OtpTokenRepository;
import vn.iotstar.service.OtpService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final SecureRandom random = new SecureRandom();

    public OtpServiceImpl(OtpTokenRepository otpTokenRepository) {
        this.otpTokenRepository = otpTokenRepository;
    }

    @Override
    @Transactional
    public String generateAndSaveOtp(String email, String type) {
        String otpCode = String.format("%06d", random.nextInt(1000000));

        OtpToken otpToken = OtpToken.builder()
                .email(email)
                .otpCode(otpCode)
                .type(type)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        otpTokenRepository.save(otpToken);
        return otpCode;
    }

    @Override
    @Transactional
    public boolean validateOtp(String email, String otpCode, String type) {
        Optional<OtpToken> optionalOtp = otpTokenRepository
                .findByEmailAndOtpCodeAndTypeAndUsedFalse(email, otpCode, type);

        if (optionalOtp.isPresent()) {
            OtpToken otpToken = optionalOtp.get();
            if (otpToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                otpToken.setUsed(true);
                otpTokenRepository.save(otpToken);
                return true;
            }
        }
        return false;
    }
}
