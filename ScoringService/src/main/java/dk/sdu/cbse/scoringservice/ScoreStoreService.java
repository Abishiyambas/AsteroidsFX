package dk.sdu.cbse.scoringservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ScoreStoreService {

    private static final Logger LOGGER = Logger.getLogger(ScoreStoreService.class.getName());

    private final CopyOnWriteArrayList<Integer> scores = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private final Path storagePath;

    public ScoreStoreService(
            ObjectMapper objectMapper,
            @Value("${scoring.file:data/scoring/scores.json}") String storageFile
    ) {
        this.objectMapper = objectMapper;
        this.storagePath = Paths.get(storageFile);
        loadFromDisk();
    }

    public ScoreBoardResponse addScore(int score) {
        scores.add(score);
        saveToDisk();
        return getScoreBoard();
    }

    public ScoreBoardResponse getScoreBoard() {
        return new ScoreBoardResponse(new ArrayList<>(scores), getLatestScore());
    }

    public HighScoreResponse getHighScore() {
        int highScore = scores.stream().mapToInt(Integer::intValue).max().orElse(0);
        return new HighScoreResponse(highScore);
    }

    public void clear() {
        scores.clear();
        saveToDisk();
    }

    public int getLatestScore() {
        return scores.isEmpty() ? 0 : scores.get(scores.size() - 1);
    }

    private void loadFromDisk() {
        if (!Files.exists(storagePath)) {
            return;
        }
        try {
            ScoreFileData data = objectMapper.readValue(storagePath.toFile(), ScoreFileData.class);
            scores.clear();
            if (data.scores() != null) {
                scores.addAll(data.scores());
            }
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Could not read score file " + storagePath + ". Starting with empty scores.", exception);
        }
    }

    private void saveToDisk() {
        try {
            Path parent = storagePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(storagePath.toFile(), new ScoreFileData(new ArrayList<>(scores)));
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Could not persist scores to " + storagePath + ".", exception);
        }
    }
}
