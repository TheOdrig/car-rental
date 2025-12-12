package com.akif.payment.internal.dto;

import com.akif.shared.enums.CurrencyType;

import java.math.BigDecimal;

public record CheckoutSessionRequest(
        Long rentalId,
        BigDecimal amount,
        CurrencyType currency,
        String customerEmail,
        String description,
        String successUrl,
        String cancelUrl
) {
}
