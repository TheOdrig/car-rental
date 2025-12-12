package com.akif.rental.api;

import java.time.LocalDate;

public record RentalSummaryDto(
    Long id,
    Long carId,
    Long userId,
    String carBrand,
    String carModel,
    String carLicensePlate,
    String userEmail,
    String userFullName,
    LocalDate startDate,
    LocalDate endDate,
    boolean hasDamageReports,
    int damageReportsCount
) {}
