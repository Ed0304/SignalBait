package signalbait.service;
import signalbait.dto.nlp.NlpResult;
public interface NlpService {

    NlpResult analyze(String content);
}
