package com.akif.rental.internal.dto.report;

import com.akif.rental.domain.enums.LateReturnStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LateReturnFilterDto(

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate,

    LateReturnStatus status,
    String sortBy,
    String sortDirection
) {

    public LateReturnFilterDto {
        sortBy = (sortBy != null) ? sortBy : "endDate";
        sortDirection = (sortDirection != null) ? sortDirection : "DESC";
    }
}
