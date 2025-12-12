package com.akif.rental.internal.service.penalty.impl;

import com.akif.payment.api.PaymentStatus;
import com.akif.rental.domain.model.Payment;
import com.akif.rental.domain.model.Rental;
import com.akif.rental.internal.repository.PaymentRepository;
import com.akif.payment.api.PaymentService;
import com.akif.payment.api.PaymentResult;
import com.akif.rental.internal.service.penalty.PenaltyPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PenaltyPaymentServiceImpl implements PenaltyPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public Payment createPenaltyPayment(Rental rental, BigDecimal penaltyAmount) {
        log.debug("Creating penalty payment for rental: {} with amount: {} {}", 
                 rental.getId(), penaltyAmount, rental.getCurrency());

        if (penaltyAmount == null || penaltyAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Penalty amount must be positive");
        }

        Payment penaltyPayment = Payment.builder()
                .rental(rental)
                .amount(penaltyAmount)
                .currency(rental.getCurrency())
                .status(PaymentStatus.PENDING)
                .paymentMethod("PENALTY")
                .isDeleted(false)
                .build();

        Payment savedPayment = paymentRepository.save(penaltyPayment);
        
        log.info("Created penalty payment: ID={}, Rental ID={}, Amount={} {}", 
                savedPayment.getId(), rental.getId(), penaltyAmount, rental.getCurrency());

        return savedPayment;
    }

    @Override
    @Transactional
    public PaymentResult chargePenalty(Payment penaltyPayment) {
        log.debug("Attempting to charge penalty payment: {}", penaltyPayment.getId());

        if (penaltyPayment.getRental() == null) {
            throw new IllegalArgumentException("Penalty payment must have associated rental");
        }

        try {
            String customerId = penaltyPayment.getRental().getUserId().toString();

            PaymentResult authorizeResult = paymentService.authorize(
                    penaltyPayment.getAmount(),
                    penaltyPayment.getCurrency(),
                    customerId
            );

            if (!authorizeResult.success()) {
                log.warn("Failed to authorize penalty payment: ID={}, Reason: {}", 
                        penaltyPayment.getId(), authorizeResult.message());
                
                penaltyPayment.updateStatus(PaymentStatus.FAILED);
                penaltyPayment.setFailureReason(authorizeResult.message());
                penaltyPayment.setGatewayResponse(authorizeResult.message());
                paymentRepository.save(penaltyPayment);
                
                return authorizeResult;
            }

            PaymentResult captureResult = paymentService.capture(
                    authorizeResult.transactionId(),
                    penaltyPayment.getAmount()
            );

            if (captureResult.success()) {
                penaltyPayment.updateStatus(PaymentStatus.CAPTURED);
                penaltyPayment.setTransactionId(captureResult.transactionId());
                penaltyPayment.setGatewayResponse(captureResult.message());
                paymentRepository.save(penaltyPayment);

                log.info("Successfully charged penalty payment: ID={}, Transaction ID={}", 
                        penaltyPayment.getId(), captureResult.transactionId());
                
                return captureResult;

            } else {
                log.warn("Failed to capture penalty payment: ID={}, Reason: {}", 
                        penaltyPayment.getId(), captureResult.message());
                
                penaltyPayment.updateStatus(PaymentStatus.FAILED);
                penaltyPayment.setFailureReason(captureResult.message());
                penaltyPayment.setGatewayResponse(captureResult.message());
                paymentRepository.save(penaltyPayment);

            }
            return captureResult;
        } catch (Exception e) {
            log.error("Exception while charging penalty payment: ID={}, Error: {}", 
                     penaltyPayment.getId(), e.getMessage(), e);
            
            penaltyPayment.updateStatus(PaymentStatus.FAILED);
            penaltyPayment.setFailureReason(e.getMessage());
            paymentRepository.save(penaltyPayment);

            return PaymentResult.failure("Payment gateway error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleFailedPenaltyPayment(Payment penaltyPayment) {
        log.debug("Handling failed penalty payment: {}", penaltyPayment.getId());

        penaltyPayment.updateStatus(PaymentStatus.PENDING);
        paymentRepository.save(penaltyPayment);

        log.warn("[ADMIN NOTIFICATION] Failed penalty payment requires manual processing: " +
                "Payment ID={}, Rental ID={}, Amount={} {}, Customer Email={}, Failure Reason: {}", 
                penaltyPayment.getId(),
                penaltyPayment.getRental().getId(),
                penaltyPayment.getAmount(),
                penaltyPayment.getCurrency(),
                penaltyPayment.getRental().getUserEmail(),
                penaltyPayment.getFailureReason());

        log.info("Marked penalty payment as PENDING for manual processing: ID={}", 
                penaltyPayment.getId());
    }
}
