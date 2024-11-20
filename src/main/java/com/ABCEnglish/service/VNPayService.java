package com.ABCEnglish.service;

import com.ABCEnglish.configuration.VNPayConfig;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.CourseOfUser;
import com.ABCEnglish.entity.PaymentInfo;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.reponsesitory.CourseOfUserRepository;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VNPayService {

    @Autowired
    private VNPayConfig vnpayConfig;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CourseOfUserRepository courseOfUserRepository;

    // Temporary storage for payment information
    private Map<String, PaymentInfo> temporaryStorage = new HashMap<>();

    // Generate VNPay payment URL
    public String createPaymentUrl(Integer courseId, String ipAddress, IntrospectRequest token) throws Exception {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        try {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnpayConfig.getTmnCode());

            // Calculate payment amount in VND (multiply by 100 for VNPay)
            BigDecimal amount = new BigDecimal(course.getFee())
                    .multiply(BigDecimal.valueOf(100))
                    .multiply(BigDecimal.valueOf(22000)); // Convert to VND

            vnp_Params.put("vnp_Amount", amount.toString());
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", generateTxnRef(courseId));
            vnp_Params.put("vnp_OrderInfo", "Payment for course: " + course.getName());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn"); // Language: Vietnamese
            vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
            vnp_Params.put("vnp_IpAddr", ipAddress);

            // Generate date and expire time
            String createDate = getCurrentDate();
            vnp_Params.put("vnp_CreateDate", createDate);
            String expireDate = getExpireDate(createDate);
            vnp_Params.put("vnp_ExpireDate", expireDate);

            // Build the query string for the payment request
            String query = buildQueryString(vnp_Params);

            // Calculate the secure hash
            String secureHash = calculateHMACSHA512(query, vnpayConfig.getHashSecret());

            // Add secure hash to the query string
            query += "&vnp_SecureHash=" + secureHash;

            // Store payment information in temporary storage
            temporaryStorage.put(vnp_Params.get("vnp_TxnRef"), new PaymentInfo(userId, courseId));

            // Return the full VNPay URL
            return vnpayConfig.getUrl() + "?" + query;
        } catch (Exception e) {
            throw new AppException(ErrorCode.PAYMENT_URL_CREATION_FAILED);
        }
    }

    // Generate the transaction reference string
    private String generateTxnRef(Integer courseId) {
        return courseId + "_" + System.currentTimeMillis();
    }

    // Get the current date in "yyyyMMddHHmmss" format
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return formatter.format(new Date());
    }

    // Get the expiration date (15 minutes after the creation time)
    private String getExpireDate(String createDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return formatter.format(new Date(System.currentTimeMillis() + 300000)); // 15 minutes
    }

    // Build the query string from the parameters map
    private String buildQueryString(Map<String, String> vnp_Params) {
        return vnp_Params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // Sort parameters alphabetically
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    // Calculate HMACSHA512 hash for the given data and secret key
    private String calculateHMACSHA512(String data, String secretKey) throws Exception {
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512_HMAC.init(secretKeySpec);
        byte[] hashBytes = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Process the VNPay response after payment
    public String processVNPayResponse(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");
        PaymentInfo paymentInfo = temporaryStorage.get(txnRef);

        if (paymentInfo == null) {
            return "Invalid transaction reference!";
        }

        // Verify the security hash from VNPay
        String secureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash"); // Remove secure hash from params to calculate it again

        String query = buildQueryString(params);

        try {
            String calculatedHash = calculateHMACSHA512(query, vnpayConfig.getHashSecret());

            if (!calculatedHash.equals(secureHash)) {
                return "Invalid payment signature!";
            }

            String responseCode = params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                // Payment success, save course of user
                saveCourseOfUser(paymentInfo);
                temporaryStorage.remove(txnRef); // Remove from temporary storage

                return "Payment Success! Ref: " + txnRef + ", Amount: " + params.get("vnp_Amount");
            } else {
                return "Payment Failed! Ref: " + txnRef + ", Amount: " + params.get("vnp_Amount");
            }
        } catch (Exception e) {
            return "Error calculating signature!";
        }
    }

    // Save the course of user after successful payment
    private void saveCourseOfUser(PaymentInfo paymentInfo) {
        Course course = courseRepository.findById(paymentInfo.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        User user = userRepository.findById(paymentInfo.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        CourseOfUser courseOfUser = new CourseOfUser();
        courseOfUser.setUser(user);
        courseOfUser.setCourse(course);
        courseOfUserRepository.save(courseOfUser);
    }
}