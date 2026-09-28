package signalbait.controller;

import org.springframework.web.bind.annotation.*;

import signalbait.dto.TicketRequest;
import signalbait.model.Ticket;
import signalbait.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public Ticket createTicket(@RequestBody TicketRequest request) {

        return ticketService.createTicket(
            request.getIssueType()
        );
    }
}