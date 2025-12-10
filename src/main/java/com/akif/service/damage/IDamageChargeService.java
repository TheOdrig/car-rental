package com.akif.service.damage;

import com.akif.model.DamageReport;
import com.akif.rental.domain.model.Payment;
import com.akif.service.gateway.PaymentResult;

public interface IDamageChargeService {

    Payment createDamageCharge(DamageReport damageReport);

    PaymentResult chargeDamage(Payment damagePayment);

    void handleFailedDamageCharge(Payment damagePayment);
}
