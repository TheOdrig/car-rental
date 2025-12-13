package com.akif.rental.internal.dto.report;

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
