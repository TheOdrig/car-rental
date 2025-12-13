package com.akif.car.internal.service.availability;

import com.akif.car.internal.dto.availability.AvailabilitySearchRequest;
import com.akif.car.internal.dto.availability.AvailabilitySearchResponse;
import com.akif.car.internal.dto.availability.CarAvailabilityCalendarDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CarAvailabilityService {

    AvailabilitySearchResponse searchAvailableCars(AvailabilitySearchRequest request);

    boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate);

    CarAvailabilityCalendarDto getCarAvailabilityCalendar(Long carId, YearMonth month);

    List<LocalDate> getUnavailableDates(Long carId, LocalDate startDate, LocalDate endDate);
}
