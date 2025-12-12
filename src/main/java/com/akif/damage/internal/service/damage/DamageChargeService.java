package com.akif.damage.internal.service.damage;

import com.akif.damage.domain.model.DamageReport;
import com.akif.payment.api.PaymentResult;

public interface DamageChargeService {

    Object createDamageCharge(DamageReport damageReport);

    PaymentResult chargeDamage(Object damagePayment);

    void handleFailedDamageCharge(Object damagePayment);
}

