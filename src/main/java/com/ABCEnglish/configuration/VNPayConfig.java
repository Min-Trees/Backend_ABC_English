package com.ABCEnglish.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class VNPayConfig {

    private String tmnCode = "FM2YJ74Z";  // Mã TMN của VNPay
    private String hashSecret = "M2NXGKZG9WNZ0X8HSRX56TFO6XMWCL75";  // Bí mật hash của VNPay
    private String url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";  // URL VNPay Sandbox
    private String returnUrl = "http://localhost:8080/payment/vnpay_return";  // URL trả về sau khi thanh toán
}
