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


}
