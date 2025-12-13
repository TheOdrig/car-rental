package com.akif.car.internal.dto.availability;

import com.akif.shared.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record AvailableCarDto(

    Long id,
    String brand,
    String model,
    Integer productionYear,
    String bodyType,
    String fuelType,
    String transmissionType,
    Integer seats,
    String imageUrl,
    BigDecimal rating,

    BigDecimal dailyRate,
    BigDecimal totalPrice,
    CurrencyType currency,
    List<String> appliedDiscounts
) {}
