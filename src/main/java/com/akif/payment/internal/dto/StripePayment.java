package com.akif.payment.internal.dto;

import java.math.BigDecimal;

public record StripePayment(
    String chargeId,
    String paymentIntentId,
    BigDecimal amount,
    String currency,
    String status
) {}
