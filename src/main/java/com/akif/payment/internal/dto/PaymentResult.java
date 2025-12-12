package com.akif.payment.internal.dto;

public record PaymentResult(
        boolean success,
        String transactionId,
        String message
) {
    public static PaymentResult success(String transactionId, String message) {
        return new PaymentResult(true, transactionId, message);
    }

    public static PaymentResult failure(String message) {
        return new PaymentResult(false, null, message);
    }
}
