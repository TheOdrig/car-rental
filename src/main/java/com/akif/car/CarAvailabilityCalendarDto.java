package com.akif.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.YearMonth;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarAvailabilityCalendarDto(

    Long carId,
    String carName,

    @JsonFormat(pattern = "yyyy-MM")
    YearMonth month,

    List<DayAvailabilityDto> days,
    Boolean carBlocked,
    String blockReason
) {}
