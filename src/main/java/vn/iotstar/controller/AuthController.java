package vn.iotstar.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.*;
import vn.iotstar.service.AuthService;
import vn.iotstar.service.OtpService;

@Controller
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDTO") RegisterDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(dto);
            redirectAttributes.addFlashAttribute("email", dto.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", "Mã OTP xác thực đã được gửi đến email của bạn.");
            return "redirect:/verify-otp?email=" + dto.getEmail();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("registerDTO", dto);
            return "redirect:/register";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam(value = "email", required = false) String email, Model model) {
        VerifyOtpDTO verifyOtpDTO = VerifyOtpDTO.builder()
                .email(email)
                .type("REGISTER")
                .build();
        model.addAttribute("verifyOtpDTO", verifyOtpDTO);
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(
            @Valid @ModelAttribute("verifyOtpDTO") VerifyOtpDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/verify-otp";
        }

        try {
            boolean verified = authService.verifyRegistrationOtp(dto);
            if (verified) {
                redirectAttributes.addFlashAttribute("verified", true);
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Mã OTP không hợp lệ, hết hạn hoặc quá số lần thử.");
                return "redirect:/verify-otp?email=" + dto.getEmail();
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/verify-otp?email=" + dto.getEmail();
        }
    }

    @PostMapping("/resend-register-otp")
    public String resendRegisterOtp(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            otpService.generateAndSaveOtp(email, "REGISTER");
            redirectAttributes.addFlashAttribute("successMessage", "Đã gửi lại mã OTP thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @Valid @ModelAttribute("forgotPasswordDTO") ForgotPasswordDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/forgot-password";
        }

        try {
            authService.processForgotPassword(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Mã OTP đặt lại mật khẩu đã được gửi.");
            return "redirect:/reset-password?email=" + dto.getEmail();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(value = "email", required = false) String email, Model model) {
        ResetPasswordDTO dto = ResetPasswordDTO.builder()
                .email(email)
                .build();
        model.addAttribute("resetPasswordDTO", dto);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @Valid @ModelAttribute("resetPasswordDTO") ResetPasswordDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/reset-password";
        }

        try {
            boolean reset = authService.resetPassword(dto);
            if (reset) {
                redirectAttributes.addFlashAttribute("reset", true);
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Mã OTP không hợp lệ hoặc đã hết hạn.");
                return "redirect:/reset-password?email=" + dto.getEmail();
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reset-password?email=" + dto.getEmail();
        }
    }
}
