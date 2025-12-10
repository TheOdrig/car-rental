package com.akif.auth;

public record LinkAccountResponse(

    String message,
    String provider,
    String providerEmail,
    String linkedAt
) {}
