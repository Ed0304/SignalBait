package signalbait.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TicketEmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String trackingUrl;

    public TicketEmailService(
            JavaMailSender mailSender,
            @Value("${signalbait.mail.from}") String fromAddress,
            @Value("${signalbait.tracking-url}") String trackingUrl) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.trackingUrl = trackingUrl;
    }

    public boolean sendTicketConfirmation(String recipient, Long ticketNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(recipient);
        message.setSubject("SignalBait report received — ticket #" + ticketNumber);
        message.setText(
                "Thanks for helping improve SignalBait.\n\n"
                + "Your report has been received. Your ticket number is: " + ticketNumber + "\n\n"
                + "Keep this number and the email address you submitted to check your ticket status.\n"
                + "Track your ticket: " + trackingUrl + "\n\n"
                + "This is an automated confirmation; please do not reply.");

        try {
            mailSender.send(message);
            return true;
        } catch (MailException exception) {
            // The ticket has already been saved. Do not make the user retry and create duplicates.
            return false;
        }
    }
    public boolean sendStatusUpdate(String recipient, Long ticketNumber, String newStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(recipient);
        message.setSubject("SignalBait ticket #" + ticketNumber + " status updated");
        message.setText(
                "Your SignalBait report has a status update.\n\n"
                + "Ticket number: " + ticketNumber + "\n"
                + "New status: " + newStatus + "\n\n"
                + "You can check your ticket here: " + trackingUrl + "\n\n"
                + "This is an automated notification; please do not reply.");
        try {
            mailSender.send(message);
            return true;
        } catch (MailException exception) {
            // Status changes should not be rolled back just because email delivery failed.
            return false;
        }
    }

}

