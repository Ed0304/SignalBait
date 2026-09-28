package signalbait.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import signalbait.model.Ticket;

public interface TicketRepository
    extends JpaRepository<Ticket, Long> {
}
