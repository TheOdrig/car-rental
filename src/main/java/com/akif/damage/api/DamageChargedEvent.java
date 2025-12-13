package com.akif.damage.api;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DamageChargedEvent extends ApplicationEvent {

    private final Long damageReportId;
    private final Long rentalId;
    private final String customerEmail;
    private final BigDecimal chargeAmount;
    private final String transactionId;
    private final LocalDateTime chargedAt;

    public DamageChargedEvent(Object source,
                              Long damageReportId,
                              Long rentalId,
                              String customerEmail,
                              BigDecimal chargeAmount,
                              String transactionId,
                              LocalDateTime chargedAt) {
        super(source);
        this.damageReportId = damageReportId;
        this.rentalId = rentalId;
        this.customerEmail = customerEmail;
        this.chargeAmount = chargeAmount;
        this.transactionId = transactionId;
        this.chargedAt = chargedAt;
    }
}
