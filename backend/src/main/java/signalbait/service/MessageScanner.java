package signalbait.service;

import org.springframework.stereotype.Service;

import signalbait.dto.nlp.NlpResult;
import signalbait.service.NlpService;

@Service
public class MessageScanner {
    private final NlpService nlpService;

    public MessageScanner(NlpService nlpService){
        this.nlpService = nlpService;
    }

    public NlpResult analyze(String content){
        return nlpService.analyze(content);
    }
}
