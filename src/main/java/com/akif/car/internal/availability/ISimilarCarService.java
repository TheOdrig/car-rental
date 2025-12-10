package com.akif.car.internal.availability;

import com.akif.car.SimilarCarDto;

import java.time.LocalDate;
import java.util.List;

public interface ISimilarCarService {

    List<SimilarCarDto> findSimilarAvailableCars(Long carId, LocalDate startDate, LocalDate endDate, int limit);
}
