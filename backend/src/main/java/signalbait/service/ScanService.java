package signalbait.service;

import org.springframework.stereotype.Service;
import signalbait.dto.ScanResponse;

// Tells Spring to manage this class as a service component.
// Business/application logic will live here.
@Service 
public class ScanService {

    public ScanResponse scan(String content){
        content = "";
        return new ScanResponse(
            "HIGH",
            87,
            "Potential phishing attempt detected."
        );
    }
    
    
    
}
