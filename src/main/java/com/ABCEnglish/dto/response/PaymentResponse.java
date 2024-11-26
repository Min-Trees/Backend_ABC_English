package com.ABCEnglish.dto.response;

public class PaymentResponse {

    private String message;
    private String paymentUrl;
    private Boolean success;

    public PaymentResponse(String message, String paymentUrl, Boolean success) {
        this.message = message;
        this.paymentUrl = paymentUrl;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
