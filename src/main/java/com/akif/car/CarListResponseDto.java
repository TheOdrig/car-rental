package com.akif.car;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarListResponseDto(

    List<CarResponse> cars,
    Long totalElements,
    Integer totalPages,
    Integer currentPage,
    Integer pageSize,
    Boolean isFirst,
    Boolean isLast,
    Boolean hasNext,
    Boolean hasPrevious,
    Integer numberOfElements
) {}
