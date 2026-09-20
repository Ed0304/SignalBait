package signalbait.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import signalbait.dto.ScanRequest;
import signalbait.dto.ScanResponse;
import signalbait.service.ScanService;
// Tells Spring that this class handles HTTP requests.
// Return values from methods are written directly to the HTTP response body.
@RestController
@RequestMapping("/api/scan")
public class SignalbaitController {
    //SignalbaitController class depends on ScanService class
    private final ScanService scanService;
    //final: a variable can't be reassigned after it has been initialized.

    // Constructor injection:
    // Spring automatically provides the ScanService instance here.
    public SignalbaitController(ScanService scanService) {
        this.scanService = scanService; 
        //No new scanService, because the constructor depends on Scanservice type of object.
    }

    // Handles POST HTTP requests.
    // @RequestBody converts the incoming JSON body into a ScanRequest object.
    @PostMapping
    // @Valid validates the object the function receives
    public ScanResponse scan(@Valid @RequestBody ScanRequest request) {

        // Controller handles HTTP.
        // Service handles the actual application logic.
        return scanService.scan(request.getContent());

    }
}