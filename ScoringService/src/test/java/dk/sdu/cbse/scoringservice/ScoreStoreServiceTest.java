package dk.sdu.cbse.scoringservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreStoreServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void addScoreUpdatesHighScoreAndKeepsHistory() {
        ScoreStoreService service = new ScoreStoreService(
                new ObjectMapper(),
                tempDir.resolve("scores.json").toString()
        );

        service.addScore(100);
        ScoreBoardResponse response = service.addScore(300);

        assertEquals(2, response.scores().size());
        assertEquals(300, response.score());
        assertEquals(300, service.getHighScore().highScore());
    }

    @Test
    void scoresAreLoadedAgainAfterServiceRestart() {
        Path scoreFile = tempDir.resolve("scores.json");

        ScoreStoreService firstInstance = new ScoreStoreService(new ObjectMapper(), scoreFile.toString());
        firstInstance.addScore(200);
        firstInstance.addScore(450);

        ScoreStoreService secondInstance = new ScoreStoreService(new ObjectMapper(), scoreFile.toString());
        ScoreBoardResponse response = secondInstance.getScoreBoard();

        assertEquals(2, response.scores().size());
        assertEquals(450, response.score());
        assertEquals(450, secondInstance.getHighScore().highScore());
    }
}
