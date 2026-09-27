package signalbait.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketNumber;

    @Column(nullable = false)
    private String issueType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Ticket() {
    }

    public Ticket(String issueType) {
        this.issueType = issueType;
        this.createdAt = LocalDateTime.now();
    }

    public Long getTicketNumber() {
        return ticketNumber;
    }

    public String getIssueType() {
        return issueType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
