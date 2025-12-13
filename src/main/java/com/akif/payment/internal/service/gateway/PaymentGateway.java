package com.akif.payment.internal.service.gateway;

import com.akif.payment.api.CheckoutSessionResult;
import com.akif.payment.api.PaymentResult;
import com.akif.shared.enums.CurrencyType;

import java.math.BigDecimal;

public interface PaymentGateway {

    PaymentResult authorize(BigDecimal amount, CurrencyType currency, String customerId);

    PaymentResult capture(String transactionId, BigDecimal amount);

    PaymentResult refund(String transactionId, BigDecimal amount);

    CheckoutSessionResult createCheckoutSession(
            Long rentalId,
            BigDecimal amount,
            CurrencyType currency,
            String customerEmail,
            String description,
            String successUrl,
            String cancelUrl
    );
}