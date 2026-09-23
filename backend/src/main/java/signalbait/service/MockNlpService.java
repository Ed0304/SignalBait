package signalbait.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import signalbait.dto.nlp.NlpResult;
import signalbait.dto.nlp.Sentiment;
import signalbait.dto.nlp.SignalCategory;

// Temporary NLP implementation.
//
// This is only here so we can test the backend architecture
// before connecting a real AI/NLP provider.
@Service
public class MockNlpService implements NlpService {

    @Override
    public NlpResult analyze(String content) {

        System.out.println("MockNlpService received: " + content);
        // Temporary result.
        // These values are NOT produced by an actual AI model yet.
         Map<SignalCategory, Double> categoryScores = Map.of(
            SignalCategory.URGENT, 0.96
        );

        return new NlpResult(
            Sentiment.NEGATIVE,
            categoryScores
        );
    }
}