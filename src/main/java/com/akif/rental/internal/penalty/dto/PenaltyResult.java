package com.akif.rental.internal.penalty.dto;

import com.akif.rental.domain.enums.LateReturnStatus;

import java.math.BigDecimal;

public record PenaltyResult(
    BigDecimal penaltyAmount,
    BigDecimal dailyRate,
    int lateHours,
    int lateDays,
    LateReturnStatus status,
    String breakdown,
    boolean cappedAtMax
) {
}
