package signalbait.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticket_id;

    @Column(name = "issue_type", nullable = false, length = 255)
    private String issue_type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "reporter_email", nullable = false)
    private String reporter_email;

    @Column(name = "ticket_status", nullable = false, length = 20)
    private String ticket_status;

    public Ticket() {
    }

    public Ticket(String issue_type, String reporter_email) {
        this.issue_type = issue_type;
        this.reporter_email = reporter_email;
        this.created_at = LocalDateTime.now();
        this.ticket_status = "NEW";
    }

    public Long getTicketNumber() {
        return ticket_id;
    }

    public String getIssueType() {
        return issue_type;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public String getReporterEmail() {
        return reporter_email;
    }

    public String getTicketStatus() {
        return ticket_status;
    }
}
