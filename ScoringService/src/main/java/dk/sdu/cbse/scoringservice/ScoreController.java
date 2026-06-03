package dk.sdu.cbse.scoringservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {

    private final ScoreStoreService scoreStoreService;

    public ScoreController(ScoreStoreService scoreStoreService) {
        this.scoreStoreService = scoreStoreService;
    }

    @PostMapping("/score")
    public ScoreBoardResponse postScore(@RequestBody ScoreSubmission submission) {
        return scoreStoreService.addScore(submission.score());
    }

    @GetMapping("/score")
    public ScoreBoardResponse getScores() {
        return scoreStoreService.getScoreBoard();
    }

    @GetMapping("/score/highscore")
    public HighScoreResponse getHighScore() {
        return scoreStoreService.getHighScore();
    }
}
