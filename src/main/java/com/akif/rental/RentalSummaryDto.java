package com.akif.rental;

import com.akif.rental.domain.enums.RentalStatus;

import java.time.LocalDate;

public record RentalSummaryDto(
        Long id,
        RentalStatus status,
        LocalDate startDate,
        LocalDate endDate,
        Long carId,
        Long userId
) {}

