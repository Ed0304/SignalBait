
package signalbait.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClientException;

@Service
public class TicketEmailService {

    private static final Logger log =
            LoggerFactory.getLogger(TicketEmailService.class);

    private static final String BREVO_API_URL =
            "https://api.brevo.com/v3/smtp/email";

    private final RestClient restClient;
    private final String apiKey;
    private final String fromAddress;
    private final String trackingUrl;

    public TicketEmailService(
            RestClient.Builder restClientBuilder,
            @Value("${brevo.api-key}") String apiKey,
            @Value("${signalbait.mail.from}") String fromAddress,
            @Value("${signalbait.tracking-url}") String trackingUrl) {

        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
        this.fromAddress = fromAddress;
        this.trackingUrl = trackingUrl;
    }

    public boolean sendTicketConfirmation(
            String recipient, Long ticketNumber) {

        String subject =
                "SignalBait report received — ticket #" + ticketNumber;

        String text =
                "Thanks for helping improve SignalBait.\n\n"
                + "Your report has been received. "
                + "Your ticket number is: " + ticketNumber + "\n\n"
                + "Keep this number and the email address you submitted "
                + "to check your ticket status.\n"
                + "Track your ticket: " + trackingUrl + "\n\n"
                + "This is an automated confirmation; please do not reply.";

        return sendEmail(recipient, subject, text, ticketNumber);
    }

    public boolean sendStatusUpdate(
            String recipient, Long ticketNumber, String newStatus) {

        String subject =
                "SignalBait ticket #" + ticketNumber + " status updated";

        String text =
                "Your SignalBait report has a status update.\n\n"
                + "Ticket number: " + ticketNumber + "\n"
                + "New status: " + newStatus + "\n\n"
                + "You can check your ticket here: " + trackingUrl + "\n\n"
                + "This is an automated notification; please do not reply.";

        return sendEmail(recipient, subject, text, ticketNumber);
    }

    private boolean sendEmail(
            String recipient,
            String subject,
            String text,
            Long ticketNumber) {

        Map<String, Object> payload = Map.of(
                "sender", Map.of(
                        "name", "SignalBait",
                        "email", fromAddress
                ),
                "to", List.of(
                        Map.of("email", recipient)
                ),
                "subject", subject,
                "textContent", text
        );

        try {
            restClient.post()
                    .uri(BREVO_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("api-key", apiKey)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Brevo accepted email request for ticket #{}",
                    ticketNumber);
            return true;

        } catch (RestClientResponseException exception) {
            log.error(
                    "Brevo rejected email for ticket #{}: HTTP {}, response: {}",
                    ticketNumber,
                    exception.getStatusCode().value(),
                    exception.getResponseBodyAsString()
            );
            return false;

        } catch (RestClientException exception) {
            log.error(
                    "Failed to contact Brevo API for ticket #{}",
                    ticketNumber,
                    exception
            );
            return false;
        }
    }
}
