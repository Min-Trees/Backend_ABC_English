package com.ABCEnglish.service;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.VerifiTokenEntity;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.ABCEnglish.reponsesitory.VerificationTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {
    JavaMailSender mailSender;
    VerificationTokenRepository tokenRepository;
    UserRepository userRepository;
    public String verifyEmail(String token){
        VerifiTokenEntity verifiTokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        User user = verifiTokenEntity.getUser();
        if(user.getStatus()){
            return "Account already verified.";
        }

        if(verifiTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())){
            return "Token expired.";
        }
        user.setStatus(true);
        userRepository.save(user);
        return "Email verified successfully.";
    }
    public String sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
        return "Send success";
    }
    public String sendVerificationCode(String to) throws MessagingException {
        String subject = "Email verification code";
        String verificationCode = generateVerificationCode();
        String content = "<p>Your verification code change password is: <b>" + verificationCode + "</b></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("ABC-English <npminhtri.be@gmail.com>");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
        return verificationCode;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }


}
