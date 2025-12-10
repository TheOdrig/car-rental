package com.akif.car;

import com.akif.shared.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimilarCarDto(

    Long id,
    String brand,
    String model,
    Integer productionYear,
    String bodyType,
    BigDecimal dailyRate,
    BigDecimal totalPrice,
    CurrencyType currency,
    String imageUrl,
    List<String> similarityReasons,
    Integer similarityScore
) {}
