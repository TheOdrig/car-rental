package com.akif.payment.api;

public record CheckoutSessionResult(
        String sessionId,
        String sessionUrl,
        String idempotencyKey
) {
}
