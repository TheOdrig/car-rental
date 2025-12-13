package com.akif.rental.internal.service.detection;

import com.akif.rental.domain.enums.LateReturnStatus;
import com.akif.rental.domain.model.Rental;

import java.time.LocalDateTime;

public interface LateReturnDetectionService {

    void detectLateReturns();

    LateReturnStatus calculateLateStatus(Rental rental, LocalDateTime currentTime);

    long calculateLateHours(Rental rental, LocalDateTime currentTime);

    long calculateLateDays(Rental rental, LocalDateTime currentTime);
}
