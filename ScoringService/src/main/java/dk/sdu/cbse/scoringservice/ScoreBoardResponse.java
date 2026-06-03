package dk.sdu.cbse.scoringservice;

import java.util.List;

public record ScoreBoardResponse(List<Integer> scores, int score) {
}
