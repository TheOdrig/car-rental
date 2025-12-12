package com.akif.payment.api;

import com.akif.payment.internal.dto.CheckoutSessionRequest;
import com.akif.shared.enums.CurrencyType;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentResult authorize(BigDecimal amount, CurrencyType currency, String customerId);

    PaymentResult capture(String transactionId, BigDecimal amount);

    PaymentResult refund(String transactionId, BigDecimal amount);

    CheckoutSessionResult createCheckoutSession(CheckoutSessionRequest request);
}
