package vn.iotstar.service;

import vn.iotstar.dto.ForgotPasswordDTO;
import vn.iotstar.dto.RegisterDTO;
import vn.iotstar.dto.ResetPasswordDTO;
import vn.iotstar.dto.VerifyOtpDTO;

public interface AuthService {
    void register(RegisterDTO dto);
    boolean verifyRegistrationOtp(VerifyOtpDTO dto);
    void processForgotPassword(ForgotPasswordDTO dto);
    boolean resetPassword(ResetPasswordDTO dto);
}
