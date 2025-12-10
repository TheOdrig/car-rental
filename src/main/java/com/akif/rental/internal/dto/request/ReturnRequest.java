package com.akif.rental.internal.dto.request;

import jakarta.validation.constraints.Size;

public record ReturnRequest(

    @Size(max = 1000, message = "Return notes cannot exceed 1000 characters")
    String notes
) {}
