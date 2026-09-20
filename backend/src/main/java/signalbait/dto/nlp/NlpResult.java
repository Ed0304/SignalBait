package signalbait.dto.nlp;

import java.util.Map;

public class NlpResult {
    private final Sentiment sentiment;

    private final Map<SignalCategory, Double> categoryScores;

    public NlpResult(
            Sentiment sentiment,
            Map<SignalCategory, Double> categoryScores) {

        this.sentiment = sentiment;
        this.categoryScores = categoryScores;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public Map<SignalCategory, Double> getCategoryScores() {
        return categoryScores;
    }
}
