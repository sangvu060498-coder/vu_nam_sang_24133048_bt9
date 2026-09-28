package vn.iotstar.service;

import vn.iotstar.entity.OtpToken;

public interface OtpService {
    String generateAndSaveOtp(String email, String type);
    boolean validateOtp(String email, String otpCode, String type);
}
