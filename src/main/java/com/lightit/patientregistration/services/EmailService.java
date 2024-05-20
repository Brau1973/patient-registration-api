package com.lightit.patientregistration.services;

import jakarta.mail.MessagingException;

public interface EmailService {
    public void sendConfirmationEmail(String to, String patientName) throws MessagingException;
}
