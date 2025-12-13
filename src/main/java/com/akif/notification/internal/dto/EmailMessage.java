package com.akif.notification.internal.dto;

import com.akif.notification.domain.EmailType;

public record EmailMessage(
    String to,
    String subject,
    String body,
    EmailType type,
    Long referenceId
) {
}
