package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.AuthenticationRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AuthenticationResponse;
import com.ABCEnglish.dto.response.IntrospectResponse;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.reponsesitory.InvalidatedTokenRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @Autowired
    UserRepository userRepository;
    MailService mailService;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws KeyLengthException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        // Tìm người dùng trong cơ sở dữ liệu theo số điện thoại
        var user = userRepository
                .findByPhone(request.getPhone())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã mã hóa trong cơ sở dữ liệu
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        //Nếu tài khoản chưa được xác thực
        if(!user.getStatus())
        {
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFICATION);
        }

        // Nếu người dùng đã đăng nhập thành công, tạo token cho người dùng
        var token = generateToken(user.getUserId());  // Truyền userId vào generateToken

        // Trả về thông tin đăng nhập của người dùng (token, thông tin người dùng...)
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .role(user.getRole()) // Lấy tên vai trò người dùng
                .userId(user.getUserId())
                .build();
    }

    private String generateToken(Integer userId) throws KeyLengthException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())  // Sử dụng userId làm subject
                .issuer("Abc_english")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("customClaim", "custom")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }


    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        Integer userId = null;

        try{
            SignedJWT signedJWT = verifyToken(token,false);
            userId = Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject());
            System.out.println("Extracted UserID from Token: " + userId);
        } catch (AppException e){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .userId(userId)
                .build();
    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);
        if(!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
    String sendVerificationEmail(User user, String token) throws MessagingException {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User or email cannot be null or empty");
        }

        // URL xác minh email
        String verificationUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/v1/verify/")
                .path(token)
                .toUriString();

        // Tạo body của email
        String subject = "Verify your email address";
        String body = "Please click the following link to verify your email: " + verificationUrl;
        mailService.sendEmail(user.getEmail(),subject,body);

        return verificationUrl;
    }
}
