package com.akif.payment.internal.service;

import com.akif.payment.api.PaymentService;
import com.akif.payment.internal.dto.CheckoutSessionRequest;
import com.akif.payment.api.CheckoutSessionResult;
import com.akif.payment.api.PaymentResult;
import com.akif.payment.internal.service.gateway.PaymentGateway;
import com.akif.shared.enums.CurrencyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
class PaymentServiceImpl implements PaymentService {

    private final PaymentGateway paymentGateway;

    @Override
    public PaymentResult authorize(BigDecimal amount, CurrencyType currency, String customerId) {
        log.info("Authorizing payment: amount={}, currency={}, customerId={}", amount, currency, customerId);
        return paymentGateway.authorize(amount, currency, customerId);
    }

    @Override
    public PaymentResult capture(String transactionId, BigDecimal amount) {
        log.info("Capturing payment: transactionId={}, amount={}", transactionId, amount);
        return paymentGateway.capture(transactionId, amount);
    }

    @Override
    public PaymentResult refund(String transactionId, BigDecimal amount) {
        log.info("Refunding payment: transactionId={}, amount={}", transactionId, amount);
        return paymentGateway.refund(transactionId, amount);
    }

    @Override
    public CheckoutSessionResult createCheckoutSession(CheckoutSessionRequest request) {
        log.info("Creating checkout session for rental: rentalId={}, amount={} {}", 
                request.rentalId(), request.amount(), request.currency());
        
        return paymentGateway.createCheckoutSession(
                request.rentalId(),
                request.amount(),
                request.currency(),
                request.customerEmail(),
                request.description(),
                request.successUrl(),
                request.cancelUrl()
        );
    }
}

