package com.akif.car.internal.dto;

import com.akif.car.domain.enums.CarStatusType;
import jakarta.validation.constraints.NotNull;

public record CarStatusUpdateRequest(

    @NotNull(message = "Car status type cannot be null")
    CarStatusType carStatusType,

    String reason,
    String notes
) {}
