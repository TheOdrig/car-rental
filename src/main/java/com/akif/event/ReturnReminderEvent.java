package com.akif.event;

import com.akif.rental.domain.event.RentalEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReturnReminderEvent extends RentalEvent {
    
    private final LocalDate returnDate;
    private final String returnLocation;
    private final BigDecimal dailyPenaltyRate;
    
    public ReturnReminderEvent(
            Object source,
            Long rentalId,
            String customerEmail,
            LocalDateTime occurredAt,
            LocalDate returnDate,
            String returnLocation,
            BigDecimal dailyPenaltyRate) {
        super(source, rentalId, customerEmail, occurredAt);
        this.returnDate = returnDate;
        this.returnLocation = returnLocation;
        this.dailyPenaltyRate = dailyPenaltyRate;
    }
}
