package com.akif.service.availability;

import com.akif.dto.availability.SimilarCarDto;

import java.time.LocalDate;
import java.util.List;

public interface ISimilarCarService {

    List<SimilarCarDto> findSimilarAvailableCars(Long carId, LocalDate startDate, LocalDate endDate, int limit);
}
