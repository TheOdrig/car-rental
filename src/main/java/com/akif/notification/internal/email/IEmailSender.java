package com.akif.notification.internal.email;

import com.akif.notification.EmailMessage;
import com.akif.exception.EmailSendException;

public interface IEmailSender {

    void send(EmailMessage message) throws EmailSendException;
}
