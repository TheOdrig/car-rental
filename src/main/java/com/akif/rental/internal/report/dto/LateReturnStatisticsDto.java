package com.akif.rental.internal.report.dto;

import java.math.BigDecimal;

public record LateReturnStatisticsDto(
        Integer totalLateReturns,
        Integer severelyLateCount,
        BigDecimal totalPenaltyAmount,
        BigDecimal collectedPenaltyAmount,
        BigDecimal pendingPenaltyAmount,
        Double averageLateHours,
        Double lateReturnPercentage
) {
}
