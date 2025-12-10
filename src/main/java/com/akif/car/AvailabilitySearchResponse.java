package com.akif.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AvailabilitySearchResponse(

    List<AvailableCarDto> cars,
    Long totalElements,
    Integer totalPages,
    Integer currentPage,
    Integer pageSize,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate searchStartDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate searchEndDate,

    Integer rentalDays
) {}
