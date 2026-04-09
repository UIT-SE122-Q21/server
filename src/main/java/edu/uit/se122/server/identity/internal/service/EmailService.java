package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.security.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Async
    public void sendVerificationEmail(String toEmail, String verifyLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        // BẮT BUỘC: Thay bằng email bạn đã verify trên Brevo
        message.setFrom(mailProperties.getSender());

        message.setTo(toEmail);
        message.setSubject("Xác thực tài khoản hệ thống sân cầu lông");
        message.setText("Chào bạn,\n\n" +
                "Cảm ơn bạn đã đăng ký tài khoản. Vui lòng click vào đường link bên dưới để xác thực địa chỉ email và bắt đầu đặt sân:\n" +
                verifyLink + "\n\n" +
                "Link này sẽ hết hạn sau 15 phút.\n" +
                "Trân trọng!");

        mailSender.send(message);
    }
}