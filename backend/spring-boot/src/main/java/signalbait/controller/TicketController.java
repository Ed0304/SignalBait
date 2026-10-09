package signalbait.controller;

import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import signalbait.dto.TicketRequest;
import signalbait.dto.TicketResponse;
import signalbait.dto.TicketTrackRequest;
import signalbait.dto.TicketTrackingResponse;
import signalbait.dto.StatusChangeNotificationRequest;
import signalbait.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Value("${signalbait.notification.shared-secret}")
    private String notificationSharedSecret;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest request) {
        try {
            TicketResponse response = ticketService.createTicket(
                    request.getIssueType(), request.getReporterEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @PostMapping("/track")
    public ResponseEntity<?> trackTicket(@RequestBody TicketTrackRequest request) {
        return ticketService.trackTicket(request.getTicketNumber(), request.getReporterEmail())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Ticket not found. Check the ticket number and email.")));
    }
    /** Internal endpoint for Django to call after it updates ticket_status in the shared database. */
    @PostMapping("/{ticketNumber}/status-notification")
    public ResponseEntity<?> notifyStatusChange(
            @PathVariable Long ticketNumber,
            @RequestHeader(value = "X-SignalBait-Notification-Secret", required = false) String suppliedSecret,
            @RequestBody StatusChangeNotificationRequest request) {
        if (!constantTimeEquals(notificationSharedSecret, suppliedSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized notification request."));
        }
        if (request == null || request.getPreviousStatus() == null || request.getNewStatus() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "previousStatus and newStatus are required."));
        }

        TicketService.StatusNotificationResult result = ticketService.notifyStatusChange(
                ticketNumber, request.getPreviousStatus(), request.getNewStatus());
        return switch (result.code()) {
            case "INVALID_REQUEST" -> ResponseEntity.badRequest().body(Map.of("code", result.code()));
            case "TICKET_NOT_FOUND" -> ResponseEntity.notFound().build();
            case "STATUS_MISMATCH" -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("code", result.code()));
            case "NO_STATUS_CHANGE" -> ResponseEntity.ok(Map.of("code", result.code(), "emailSent", false));
            case "NO_RECIPIENT" -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("code", result.code(), "emailSent", false));
            case "EMAIL_DELIVERY_FAILED" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("code", result.code(), "emailSent", false));
            default -> ResponseEntity.ok(Map.of("code", result.code(), "emailSent", result.emailSent()));
        };
    }

    private boolean constantTimeEquals(String expected, String actual) {
        if (expected == null || expected.isBlank() || actual == null) {
            return false;
        }
        return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
    }

}

