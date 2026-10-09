package signalbait.dto;

import java.time.LocalDateTime;

public class TicketTrackingResponse {

    private final Long ticketNumber;
    private final String issueType;
    private final LocalDateTime createdAt;
    private final String ticketStatus;

    public TicketTrackingResponse(Long ticketNumber, String issueType,
                                  LocalDateTime createdAt, String ticketStatus) {
        this.ticketNumber = ticketNumber;
        this.issueType = issueType;
        this.createdAt = createdAt;
        this.ticketStatus = ticketStatus;
    }

    public Long getTicketNumber() { return ticketNumber; }
    public String getIssueType() { return issueType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getTicketStatus() { return ticketStatus; }
}
