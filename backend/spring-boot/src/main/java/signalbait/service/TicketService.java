package signalbait.service;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import signalbait.dto.TicketResponse;
import signalbait.dto.TicketTrackingResponse;
import signalbait.model.Ticket;
import signalbait.repository.TicketRepository;

@Service
public class TicketService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern ISSUE_TYPE_PATTERN = Pattern.compile("^(FALSE_POSITIVE|FALSE_NEGATIVE|INCORRECT_MARKERS)$");

    private final TicketRepository ticketRepository;
    private final TicketEmailService ticketEmailService;

    public TicketService(TicketRepository ticketRepository, TicketEmailService ticketEmailService) {
        this.ticketRepository = ticketRepository;
        this.ticketEmailService = ticketEmailService;
    }

    public TicketResponse createTicket(String issueType, String reporterEmail) {
        String normalizedIssueType = issueType == null ? "" : issueType.trim().toUpperCase(Locale.ROOT);
        String normalizedEmail = reporterEmail == null ? "" : reporterEmail.trim();

        if (!ISSUE_TYPE_PATTERN.matcher(normalizedIssueType).matches()) {
            throw new IllegalArgumentException("A valid issue type is required.");
        }
        if (normalizedEmail.length() > 254 || !EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("A valid reporter email is required.");
        }

        Ticket saved = ticketRepository.saveAndFlush(new Ticket(normalizedIssueType, normalizedEmail));
        boolean emailSent = ticketEmailService.sendTicketConfirmation(normalizedEmail, saved.getTicketNumber());

        return new TicketResponse(saved.getTicketNumber(), saved.getIssueType(), saved.getCreatedAt(),
                saved.getTicketStatus(), emailSent);
    }

    @Transactional(readOnly = true)
    public Optional<TicketTrackingResponse> trackTicket(Long ticketNumber, String reporterEmail) {
        if (ticketNumber == null || ticketNumber < 1 || reporterEmail == null || reporterEmail.isBlank()) {
            return Optional.empty();
        }

        return ticketRepository.findById(ticketNumber)
                .filter(ticket -> ticket.getReporterEmail() != null
                        && ticket.getReporterEmail().equalsIgnoreCase(reporterEmail.trim()))
                .map(ticket -> new TicketTrackingResponse(ticket.getTicketNumber(), ticket.getIssueType(),
                        ticket.getCreatedAt(), ticket.getTicketStatus()));
    }
    public StatusNotificationResult notifyStatusChange(Long ticketNumber, String previousStatus, String newStatus) {
        if (ticketNumber == null || ticketNumber < 1 || previousStatus == null || newStatus == null
                || previousStatus.isBlank() || newStatus.isBlank()) {
            return StatusNotificationResult.invalid();
        }
        if (previousStatus.trim().equalsIgnoreCase(newStatus.trim())) {
            return StatusNotificationResult.noChange();
        }

        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketNumber);
        if (optionalTicket.isEmpty()) {
            return StatusNotificationResult.notFound();
        }
        Ticket ticket = optionalTicket.get();
        // Django updates the shared row first; only notify when the request matches the current DB state.
        if (ticket.getTicketStatus() == null
                || !ticket.getTicketStatus().equalsIgnoreCase(newStatus.trim())) {
            return StatusNotificationResult.staleStatus();
        }
        if (ticket.getReporterEmail() == null || ticket.getReporterEmail().isBlank()) {
            return StatusNotificationResult.noRecipient();
        }
        boolean sent = ticketEmailService.sendStatusUpdate(
                ticket.getReporterEmail(), ticket.getTicketNumber(), ticket.getTicketStatus());
        return sent ? StatusNotificationResult.sent() : StatusNotificationResult.deliveryFailed();
    }

    public record StatusNotificationResult(String code, boolean emailSent) {
        public static StatusNotificationResult invalid() { return new StatusNotificationResult("INVALID_REQUEST", false); }
        public static StatusNotificationResult noChange() { return new StatusNotificationResult("NO_STATUS_CHANGE", false); }
        public static StatusNotificationResult notFound() { return new StatusNotificationResult("TICKET_NOT_FOUND", false); }
        public static StatusNotificationResult staleStatus() { return new StatusNotificationResult("STATUS_MISMATCH", false); }
        public static StatusNotificationResult noRecipient() { return new StatusNotificationResult("NO_RECIPIENT", false); }
        public static StatusNotificationResult sent() { return new StatusNotificationResult("NOTIFICATION_SENT", true); }
        public static StatusNotificationResult deliveryFailed() { return new StatusNotificationResult("EMAIL_DELIVERY_FAILED", false); }
    }

}

