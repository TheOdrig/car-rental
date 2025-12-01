package com.akif.service.webhook;

import com.akif.config.StripeConfig;
import com.akif.enums.PaymentStatus;
import com.akif.enums.WebhookEventStatus;
import com.akif.exception.WebhookSignatureException;
import com.akif.model.Payment;
import com.akif.model.WebhookEvent;
import com.akif.repository.PaymentRepository;
import com.akif.repository.WebhookEventRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookHandler {

    private final StripeConfig stripeConfig;
    private final PaymentRepository paymentRepository;
    private final WebhookEventRepository webhookEventRepository;

    @Transactional
    public void handleWebhookEvent(String payload, String signature) {
        log.info("Received webhook event");

        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed", e);
            throw new WebhookSignatureException("unknown", e);
        }

        String eventId = event.getId();
        String eventType = event.getType();

        log.info("Processing webhook event: {} of type: {}", eventId, eventType);

        if (isEventAlreadyProcessed(eventId)) {
            log.info("Duplicate event detected: {}, skipping processing", eventId);
            return;
        }

        WebhookEvent webhookEvent = WebhookEvent.builder()
                .eventId(eventId)
                .eventType(eventType)
                .payload(payload)
                .status(WebhookEventStatus.PROCESSING)
                .build();
        webhookEventRepository.save(webhookEvent);

        try {
            switch (eventType) {
                case "checkout.session.completed":
                    Session completedSession = (Session) event.getDataObjectDeserializer()
                            .getObject()
                            .orElseThrow(() -> new IllegalStateException("Failed to deserialize session"));
                    processCheckoutSessionCompleted(completedSession);
                    break;

                case "checkout.session.expired":
                    Session expiredSession = (Session) event.getDataObjectDeserializer()
                            .getObject()
                            .orElseThrow(() -> new IllegalStateException("Failed to deserialize session"));
                    processCheckoutSessionExpired(expiredSession);
                    break;

                case "payment_intent.payment_failed":
                    PaymentIntent failedIntent = (PaymentIntent) event.getDataObjectDeserializer()
                            .getObject()
                            .orElseThrow(() -> new IllegalStateException("Failed to deserialize payment intent"));
                    processPaymentIntentFailed(failedIntent);
                    break;

                default:
                    log.warn("Unhandled event type: {}", eventType);
            }

            webhookEvent.setStatus(WebhookEventStatus.PROCESSED);
            webhookEvent.setProcessedAt(LocalDateTime.now());
            webhookEventRepository.save(webhookEvent);

            log.info("Successfully processed webhook event: {}", eventId);

        } catch (Exception e) {
            log.error("Error processing webhook event: {}", eventId, e);
            webhookEvent.setStatus(WebhookEventStatus.FAILED);
            webhookEvent.setErrorMessage(e.getMessage());
            webhookEventRepository.save(webhookEvent);
            throw e;
        }
    }

    public void processCheckoutSessionCompleted(Session session) {
        String sessionId = session.getId();
        log.info("Processing checkout.session.completed for session: {}", sessionId);

        Payment payment = findPaymentBySessionId(sessionId);
        
        payment.setStatus(PaymentStatus.CAPTURED);
        payment.setStripePaymentIntentId(session.getPaymentIntent());
        payment.setTransactionId(session.getPaymentIntent());
        
        paymentRepository.save(payment);
        
        log.info("Payment {} updated to CAPTURED for session: {}", payment.getId(), sessionId);
    }

    public void processCheckoutSessionExpired(Session session) {
        String sessionId = session.getId();
        log.info("Processing checkout.session.expired for session: {}", sessionId);

        Payment payment = findPaymentBySessionId(sessionId);
        
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("Checkout session expired");
        
        paymentRepository.save(payment);
        
        log.info("Payment {} updated to FAILED for session: {}", payment.getId(), sessionId);
    }

    public void processPaymentIntentFailed(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        log.info("Processing payment_intent.payment_failed for intent: {}", paymentIntentId);

        Payment payment = findPaymentByPaymentIntentId(paymentIntentId);
        
        payment.setStatus(PaymentStatus.FAILED);

        String failureReason = "Payment failed";
        if (paymentIntent.getLastPaymentError() != null) {
            failureReason = paymentIntent.getLastPaymentError().getMessage();
        }
        payment.setFailureReason(failureReason);
        
        paymentRepository.save(payment);
        
        log.info("Payment {} updated to FAILED for intent: {} with reason: {}", 
                 payment.getId(), paymentIntentId, failureReason);
    }

    public boolean isEventAlreadyProcessed(String eventId) {
        Optional<WebhookEvent> existingEvent = webhookEventRepository.findByEventId(eventId);
        
        if (existingEvent.isPresent()) {
            WebhookEventStatus status = existingEvent.get().getStatus();

            if (status == WebhookEventStatus.PROCESSED || 
                status == WebhookEventStatus.PROCESSING ||
                status == WebhookEventStatus.DUPLICATE) {

                if (status == WebhookEventStatus.PROCESSED) {
                    WebhookEvent event = existingEvent.get();
                    event.setStatus(WebhookEventStatus.DUPLICATE);
                    webhookEventRepository.save(event);
                }
                
                return true;
            }
        }
        
        return false;
    }

    private Payment findPaymentBySessionId(String sessionId) {
        return paymentRepository.findAll().stream()
                .filter(p -> sessionId.equals(p.getStripeSessionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Payment not found for session: " + sessionId));
    }

    private Payment findPaymentByPaymentIntentId(String paymentIntentId) {
        return paymentRepository.findAll().stream()
                .filter(p -> paymentIntentId.equals(p.getStripePaymentIntentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Payment not found for payment intent: " + paymentIntentId));
    }
}
