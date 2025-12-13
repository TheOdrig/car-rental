package com.akif.rental.internal.service.penalty;

import com.akif.rental.domain.model.PenaltyWaiver;

import java.math.BigDecimal;
import java.util.List;

public interface PenaltyWaiverService {

    PenaltyWaiver waivePenalty(Long rentalId, BigDecimal waiverAmount, String reason, Long adminId);

    PenaltyWaiver waiveFullPenalty(Long rentalId, String reason, Long adminId);

    List<PenaltyWaiver> getPenaltyHistory(Long rentalId);

    void processRefundForWaiver(PenaltyWaiver waiver);
}
