package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.RegisterRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.Role;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.VerifiTokenEntity;
import com.ABCEnglish.entity.VerificationToken;
import com.ABCEnglish.mapper.UserMapper;
import com.ABCEnglish.reponsesitory.RoleRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.ABCEnglish.reponsesitory.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    public UserResponse createUser(RegisterRequest request) throws MessagingException {

        Role role = roleRepository.findByName("student")
                .orElseGet(()-> {
                    Role newRole = new Role();
                    newRole.setName("student");
                    return roleRepository.save(newRole);
                });
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user = userRepository.save(user);

        String token = VerificationToken.generateToken();
        VerifiTokenEntity verifiTokenEntity = new VerifiTokenEntity(user,token);
        emailService.sendVerificationEmail(user.getEmail(), token);
        verificationTokenRepository.save(verifiTokenEntity);
        System.out.println(token);
        return userMapper.toUserResponse(user);
    }
}
