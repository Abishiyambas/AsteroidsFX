package dk.sdu.cbse.scoringservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "scoring.file=target/test-scores/controller-scores.json")
@AutoConfigureMockMvc
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScoreStoreService scoreStoreService;

    @BeforeEach
    void setUp() {
        scoreStoreService.clear();
    }

    @Test
    void restApiStoresAndReturnsHighScore() throws Exception {
        mockMvc.perform(post("/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"score\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(200));

        mockMvc.perform(post("/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"score\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(500));

        mockMvc.perform(get("/score"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.scores.length()").value(2))
                .andExpect(jsonPath("$.score").value(500));

        mockMvc.perform(get("/score/highscore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highScore").value(500));
    }
}
