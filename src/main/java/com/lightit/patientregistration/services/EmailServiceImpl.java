package com.lightit.patientregistration.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendConfirmationEmail(String to,  String patientName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Send confirmation email asynchronously
        // Prepare email content
        String subject = "Registration Confirmation";
        String htmlBody = "<h1>Welcome, " + patientName + "</h1>"
                + "<p>Thank you for registering.<br>" +
                " Best regards,<br>" +
                " Light-IT Team</p>"
                + "<img src='cid:image' style='width:200px; height:auto;' />"; // Adjust the image size here
        String imagePath = "static/images/light-it.png"; // Adjust the path to your image

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        // Attach image
        ClassPathResource resource = new ClassPathResource(imagePath);
        helper.addInline("image", resource);

        mailSender.send(message);
    }
}
