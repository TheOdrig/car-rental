package com.akif.damage.api;

import java.util.List;

public interface DamageService {

    DamageReportDto getDamageReportById(Long damageReportId);

    List<DamageReportDto> getDamageReportsByRentalId(Long rentalId);

    boolean hasPendingDamageReports(Long rentalId);

    boolean hasPendingDamageReportsForCar(Long carId);
}
