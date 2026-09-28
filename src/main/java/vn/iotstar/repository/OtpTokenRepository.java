package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.OtpToken;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmailAndOtpCodeAndTypeAndUsedFalse(String email, String otpCode, String type);
    Optional<OtpToken> findTopByEmailAndTypeAndUsedFalseOrderByExpiryDateDesc(String email, String type);
}
