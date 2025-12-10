package com.akif.car.internal.availability;

import com.akif.car.internal.availability.dto.AvailabilitySearchRequest;
import com.akif.car.AvailabilitySearchResponse;
import com.akif.car.CarAvailabilityCalendarDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ICarAvailabilityService {

    AvailabilitySearchResponse searchAvailableCars(AvailabilitySearchRequest request);

    boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate);

    CarAvailabilityCalendarDto getCarAvailabilityCalendar(Long carId, YearMonth month);

    List<LocalDate> getUnavailableDates(Long carId, LocalDate startDate, LocalDate endDate);
}
