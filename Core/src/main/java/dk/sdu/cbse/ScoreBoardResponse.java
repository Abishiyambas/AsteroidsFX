package dk.sdu.cbse;

import java.util.List;

public record ScoreBoardResponse(List<Integer> scores, int score) {
}
