package signalbait.service;

import org.springframework.stereotype.Service;

import signalbait.model.Ticket;
import signalbait.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(String issue_type) {
        Ticket ticket = new Ticket(issue_type);

        return ticketRepository.save(ticket);
    }
}