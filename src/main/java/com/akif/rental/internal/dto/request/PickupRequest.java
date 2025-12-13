package com.akif.rental.internal.dto.request;

import jakarta.validation.constraints.Size;

public record PickupRequest(

    @Size(max = 1000, message = "Pickup notes cannot exceed 1000 characters")
    String notes
) {}
