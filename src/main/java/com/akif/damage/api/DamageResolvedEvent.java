package com.akif.damage.api;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DamageResolvedEvent extends ApplicationEvent {

    private final Long damageReportId;
    private final Long rentalId;
    private final String customerEmail;
    private final BigDecimal adjustedLiability;
    private final BigDecimal refundAmount;
    private final String resolutionNotes;
    private final LocalDateTime resolvedAt;

    public DamageResolvedEvent(Object source,
                               Long damageReportId,
                               Long rentalId,
                               String customerEmail,
                               BigDecimal adjustedLiability,
                               BigDecimal refundAmount,
                               String resolutionNotes,
                               LocalDateTime resolvedAt) {
        super(source);
        this.damageReportId = damageReportId;
        this.rentalId = rentalId;
        this.customerEmail = customerEmail;
        this.adjustedLiability = adjustedLiability;
        this.refundAmount = refundAmount;
        this.resolutionNotes = resolutionNotes;
        this.resolvedAt = resolvedAt;
    }
}
