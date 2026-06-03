package dk.sdu.cbse;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App extends Application {

    private AnnotationConfigApplicationContext context;
    private Game game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(CoreSpringConfig.class);
        game = context.getBean(Game.class);
    }

    @Override
    public void start(Stage stage) {
        game.start(stage);
    }

    @Override
    public void stop() {
        if (game != null) {
            game.stop();
        }
        if (context != null) {
            context.close();
        }
    }
}
