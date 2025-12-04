package com.akif.service.availability;

import com.akif.dto.availability.AvailabilitySearchRequestDto;
import com.akif.dto.availability.AvailabilitySearchResponseDto;
import com.akif.dto.availability.CarAvailabilityCalendarDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ICarAvailabilityService {

    AvailabilitySearchResponseDto searchAvailableCars(AvailabilitySearchRequestDto request);

    boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate);

    CarAvailabilityCalendarDto getCarAvailabilityCalendar(Long carId, YearMonth month);

    List<LocalDate> getUnavailableDates(Long carId, LocalDate startDate, LocalDate endDate);
}
