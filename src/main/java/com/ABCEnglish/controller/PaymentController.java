package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.PaymentResponse;
import com.ABCEnglish.service.VNPayService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnpayService;

    // API tạo thanh toán
    @GetMapping("/pay/{courseId}")
    public ResponseEntity<PaymentResponse> createPaymentUrl(@PathVariable Integer courseId, HttpServletRequest request, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
        // Lấy địa chỉ IP của khách hàng từ request
        String token = authorizationHeader.substring("Bearer".length()).trim();
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        String clientIp = request.getRemoteAddr();

        // Gọi service để tạo URL thanh toán và trả về kết quả
        String paymentUrl = vnpayService.createPaymentUrl(courseId, clientIp, introspectRequest);

        PaymentResponse response = new PaymentResponse("Payment URL generated successfully", paymentUrl, true);
        return ResponseEntity.ok(response);
    }

    // Xử lý callback từ VNPay
    @GetMapping("/vnpay_return")
    public ResponseEntity<PaymentResponse> handleVNPayReturn(@RequestParam Map<String, String> params) {
        // Gọi service để xử lý và trả về kết quả
        String result = vnpayService.processVNPayResponse(params);

        PaymentResponse response;
        if (result.contains("Payment Success")) {
            response = new PaymentResponse("Payment successful", result, true);
        } else {
            response = new PaymentResponse("Payment failed", result, false);
        }

        return ResponseEntity.ok(response);
    }
}
