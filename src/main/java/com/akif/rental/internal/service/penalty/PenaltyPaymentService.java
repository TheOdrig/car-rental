package com.akif.rental.internal.service.penalty;

import com.akif.rental.domain.model.Payment;
import com.akif.rental.domain.model.Rental;
import com.akif.payment.api.PaymentResult;

import java.math.BigDecimal;

public interface PenaltyPaymentService {

    Payment createPenaltyPayment(Rental rental, BigDecimal penaltyAmount);

    PaymentResult chargePenalty(Payment penaltyPayment);

    void handleFailedPenaltyPayment(Payment penaltyPayment);
}
