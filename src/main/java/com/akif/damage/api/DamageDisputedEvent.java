package com.akif.damage.api;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class DamageDisputedEvent extends ApplicationEvent {

    private final Long damageReportId;
    private final Long rentalId;
    private final String customerEmail;
    private final String disputeReason;
    private final LocalDateTime disputedAt;

    public DamageDisputedEvent(Object source,
                               Long damageReportId,
                               Long rentalId,
                               String customerEmail,
                               String disputeReason,
                               LocalDateTime disputedAt) {
        super(source);
        this.damageReportId = damageReportId;
        this.rentalId = rentalId;
        this.customerEmail = customerEmail;
        this.disputeReason = disputeReason;
        this.disputedAt = disputedAt;
    }
}
