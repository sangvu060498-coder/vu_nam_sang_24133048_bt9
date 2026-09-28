package vn.iotstar.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.iotstar.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otpCode, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText("Mã xác thực OTP của bạn là: " + otpCode + "\nMã này có hiệu lực trong 5 phút.");
            mailSender.send(message);
            log.info("Mã OTP [{}] đã được gửi thành công đến email: {}", otpCode, toEmail);
        } catch (Exception e) {
            log.error("Không thể gửi email đến {}. Mã OTP được log trực tiếp tại đây: [{}] - Lỗi: {}", toEmail, otpCode, e.getMessage());
        }
    }
}
