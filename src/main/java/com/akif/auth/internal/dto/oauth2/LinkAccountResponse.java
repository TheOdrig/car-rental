package com.akif.auth.internal.dto.oauth2;

public record LinkAccountResponse(

    String message,
    String provider,
    String providerEmail,
    String linkedAt
) {}
