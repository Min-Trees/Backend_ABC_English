package com.ABCEnglish.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {
    JavaMailSender mailSender;
    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String subject = "Email verification";
        String verificationUrl = "http://localhost:8080/api/v1/verify?token=" +token;
        String content = "<p> Please click the link to below to verify your email" +
                "<a href=\"" + verificationUrl + "\"> Verify Email</a>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("ABC-English <npminhtri.be@gmail.com>"); // Thay đổi địa chỉ email nếu cần
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
