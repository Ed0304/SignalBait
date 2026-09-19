package signalbait.service;

import org.springframework.stereotype.Service;

//Tells Spring this class handles application/business logic
@Service 
public class ScanService {

    public String scan(String content){
        return content;
    }
    
}
