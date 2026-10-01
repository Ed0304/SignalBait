package signalbait.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticket_id;

    @Column(nullable = false)
    private String issue_type;

    @Column(nullable = false)
    private LocalDateTime created_at;

    public Ticket() {
    }

    public Ticket(String issue_type) {
        this.issue_type = issue_type;
        this.created_at = LocalDateTime.now();
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
}
