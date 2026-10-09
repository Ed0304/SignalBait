package signalbait.dto;

import java.time.LocalDateTime;

public class TicketResponse {

    private final Long ticketNumber;
    private final String issueType;
    private final LocalDateTime createdAt;
    private final String ticketStatus;
    private final boolean confirmationEmailSent;

    public TicketResponse(Long ticketNumber, String issueType, LocalDateTime createdAt,
                          String ticketStatus, boolean confirmationEmailSent) {
        this.ticketNumber = ticketNumber;
        this.issueType = issueType;
        this.createdAt = createdAt;
        this.ticketStatus = ticketStatus;
        this.confirmationEmailSent = confirmationEmailSent;
    }

    public Long getTicketNumber() { return ticketNumber; }
    public String getIssueType() { return issueType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getTicketStatus() { return ticketStatus; }
    public boolean isConfirmationEmailSent() { return confirmationEmailSent; }
}
