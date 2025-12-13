package com.akif.rental.internal.dto.penalty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PenaltyWaiverRequest(

    @DecimalMin(value = "0.01", message = "Waiver amount must be greater than 0")
    BigDecimal waiverAmount,

    @NotBlank(message = "Reason cannot be blank")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    String reason,

    Boolean fullWaiver
) {}
