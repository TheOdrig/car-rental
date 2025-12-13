package com.akif.payment.internal.dto;

import com.akif.payment.domain.enums.DiscrepancyType;

import java.math.BigDecimal;

public record Discrepancy(
    DiscrepancyType type,
    String paymentId,
    String stripePaymentIntentId,
    BigDecimal databaseAmount,
    BigDecimal stripeAmount,
    String databaseStatus,
    String stripeStatus,
    String description
) {}
