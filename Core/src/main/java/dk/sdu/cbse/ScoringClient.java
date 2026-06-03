package dk.sdu.cbse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ScoringClient {

    private static final Logger LOGGER = Logger.getLogger(ScoringClient.class.getName());
    private static final long RETRY_DELAY_MILLIS = 5_000;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private Instant nextAttemptAt = Instant.EPOCH;
    private int cachedHighScore;

    public ScoringClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper, String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public void submitScore(int score) {
        if (!mayAttempt()) {
            return;
        }
        try {
            String response = restClient.post()
                    .uri("/score")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(new ScoreSubmission(score)))
                    .retrieve()
                    .body(String.class);
            if (response != null && !response.isBlank()) {
                objectMapper.readValue(response, ScoreBoardResponse.class);
            }
            nextAttemptAt = Instant.EPOCH;
            fetchHighScore();
        } catch (Exception exception) {
            handleFailure("submit score", exception);
        }
    }

    public OptionalInt fetchHighScore() {
        if (!mayAttempt()) {
            return cachedHighScore > 0 ? OptionalInt.of(cachedHighScore) : OptionalInt.empty();
        }
        try {
            String response = restClient.get()
                    .uri("/score/highscore")
                    .retrieve()
                    .body(String.class);
            if (response != null && !response.isBlank()) {
                HighScoreResponse highScoreResponse = objectMapper.readValue(response, HighScoreResponse.class);
                cachedHighScore = highScoreResponse.highScore();
            }
            nextAttemptAt = Instant.EPOCH;
            return OptionalInt.of(cachedHighScore);
        } catch (Exception exception) {
            handleFailure("fetch high score", exception);
            return cachedHighScore > 0 ? OptionalInt.of(cachedHighScore) : OptionalInt.empty();
        }
    }

    public int getCachedHighScore() {
        return cachedHighScore;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private boolean mayAttempt() {
        return !Instant.now().isBefore(nextAttemptAt);
    }

    private void handleFailure(String action, Exception exception) {
        nextAttemptAt = Instant.now().plusMillis(RETRY_DELAY_MILLIS);
        LOGGER.log(Level.INFO,
                "Scoring microservice unavailable while trying to " + action + " against " + baseUrl
                        + ". Gameplay continues without remote scoring.",
                exception);
    }
}
