package com.akif.notification;

import com.akif.event.*;
import com.akif.rental.domain.event.*;

public interface EmailNotificationService {

    void sendRentalConfirmation(RentalConfirmedEvent event);

    void sendPaymentReceipt(PaymentCapturedEvent event);

    void sendPickupReminder(PickupReminderEvent event);

    void sendReturnReminder(ReturnReminderEvent event);

    void sendCancellationConfirmation(RentalCancelledEvent event);
    
    void sendGracePeriodWarning(GracePeriodWarningEvent event);
    
    void sendLateReturnNotification(LateReturnNotificationEvent event);
    
    void sendSeverelyLateNotification(SeverelyLateNotificationEvent event);
    
    void sendPenaltySummary(PenaltySummaryEvent event);
    

    void sendDamageReportedNotification(DamageReportedEvent event);
    
    void sendDamageAssessedNotification(DamageAssessedEvent event);
    
    void sendDamageChargedNotification(DamageChargedEvent event);
    
    void sendDamageDisputedNotification(DamageDisputedEvent event);
    
    void sendDamageResolvedNotification(DamageResolvedEvent event);
}

