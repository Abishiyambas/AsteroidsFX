package dk.sdu.cbse;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessorService;
import dk.sdu.cbse.spi.IGamePluginService;
import dk.sdu.cbse.spi.IPostEntityProcessorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Text hudText = new Text();
    private final Text statusText = new Text();
    private final Text healthLabel = new Text();
    private final Rectangle healthBarBackground = new Rectangle();
    private final Rectangle healthBarFill = new Rectangle();
    private final Text overlayText = new Text();
    private final Button actionButton = new Button("Start");
    private final List<IGamePluginService> gamePlugins;
    private final List<IEntityProcessorService> processors;
    private final List<IPostEntityProcessorService> postProcessors;
    private final ScoringClient scoringClient;

    private AnimationTimer timer;
    private boolean gameRunning;
    private boolean gameOver;
    private int lastReportedScore;

    public Game(
            List<IGamePluginService> gamePlugins,
            List<IEntityProcessorService> processors,
            List<IPostEntityProcessorService> postProcessors,
            ScoringClient scoringClient
    ) {
        this.gamePlugins = gamePlugins;
        this.processors = processors;
        this.postProcessors = postProcessors;
        this.scoringClient = scoringClient;
        this.lastReportedScore = -1;
    }

    public void start(Stage stage) {
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: blue;");
        setupHud();

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        stage.setScene(scene);
        stage.setTitle("ASTEROIDS");
        stage.setOnCloseRequest(event -> stop());
        stage.show();

        showStartOverlay();
        startLoop();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        stopPlugins();
    }

    private void startLoop() {
        timer = new AnimationTimer() {
            private long lastTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double delta = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                gameData.setDelta(delta);
                update();
                draw();
                gameData.getKeys().update();
            }
        };
        timer.start();
    }

    private void update() {
        if (!gameRunning) {
            updateHud();
            return;
        }

        for (IEntityProcessorService service : processors) {
            service.process(gameData, world);
        }
        for (IPostEntityProcessorService service : postProcessors) {
            service.process(gameData, world);
        }

        regeneratePlayerHealth();
        syncRemoteScore();

        if (!hasPlayer()) {
            gameRunning = false;
            gameOver = true;
            showResetOverlay();
        }
        updateHud();
    }

    private void draw() {
        for (Entity polygonEntity : new ArrayList<>(polygons.keySet())) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.remove(polygonEntity);
                if (removedPolygon != null) {
                    gameWindow.getChildren().remove(removedPolygon);
                }
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = createPolygon(entity);
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
    }

    private void setupHud() {
        hudText.setFill(Color.WHITE);
        hudText.setFont(Font.font("Monospaced", 18));
        hudText.setTranslateX(10);
        hudText.setTranslateY(25);

        statusText.setFill(Color.WHITE);
        statusText.setFont(Font.font("Monospaced", 22));
        statusText.setTranslateX(10);
        statusText.setTranslateY(55);

        healthLabel.setFill(Color.WHITE);
        healthLabel.setFont(Font.font("Monospaced", 14));
        healthLabel.setTranslateX(10);
        healthLabel.setTranslateY(78);

        healthBarBackground.setX(70);
        healthBarBackground.setY(64);
        healthBarBackground.setWidth(120);
        healthBarBackground.setHeight(14);
        healthBarBackground.setFill(Color.DARKRED);
        healthBarBackground.setStroke(Color.WHITE);

        healthBarFill.setX(70);
        healthBarFill.setY(64);
        healthBarFill.setHeight(14);
        healthBarFill.setFill(Color.LIMEGREEN);

        overlayText.setFill(Color.WHITE);
        overlayText.setFont(Font.font("Monospaced", 28));
        overlayText.setTranslateX(220);
        overlayText.setTranslateY(240);

        actionButton.setLayoutX(330);
        actionButton.setLayoutY(280);
        actionButton.setPrefWidth(140);
        actionButton.setOnAction(event -> {
            if (!gameRunning) {
                startGameSession();
            }
        });

        gameWindow.getChildren().addAll(
                hudText, statusText, healthLabel, healthBarBackground, healthBarFill, overlayText, actionButton
        );
        updateHud();
    }

    private void updateHud() {
        hudText.setText(
                "Score: " + gameData.getScore()
                        + "   High Score: " + scoringClient.getCachedHighScore()
                        + "   Asteroids: " + gameData.getDestroyedAsteroids()
                        + "   Level: " + gameData.getLevel() + "/" + gameData.getMaxLevel()
        );

        if (gameData.isGameWon()) {
            statusText.setText("All 3 levels cleared");
        } else if (gameData.getLevel() == gameData.getMaxLevel()) {
            statusText.setText("Final level");
        } else {
            statusText.setText("");
        }

        int currentHealth = getPlayerHealth();
        double ratio = currentHealth / (double) gameData.getMaxPlayerHealth();
        healthLabel.setText("Health");
        healthBarFill.setWidth(Math.max(0, 120 * ratio));

        hudText.toFront();
        statusText.toFront();
        healthLabel.toFront();
        healthBarBackground.toFront();
        healthBarFill.toFront();
        overlayText.toFront();
        actionButton.toFront();
    }

    private int getPlayerHealth() {
        Entity player = findPlayer();
        return player == null ? 0 : Math.max(0, player.getHealth());
    }

    private void regeneratePlayerHealth() {
        Entity player = findPlayer();
        if (player == null || player.getHealth() >= gameData.getMaxPlayerHealth()) {
            gameData.setHealthRegenTimer(0);
            return;
        }

        gameData.setHealthRegenTimer(gameData.getHealthRegenTimer() + gameData.getDelta());
        if (gameData.getHealthRegenTimer() >= gameData.getHealthRegenInterval()) {
            player.setHealth(Math.min(gameData.getMaxPlayerHealth(), player.getHealth() + 1));
            gameData.setHealthRegenTimer(0);
        }
    }

    private void syncRemoteScore() {
        int currentScore = gameData.getScore();
        if (currentScore <= 0 || currentScore == lastReportedScore) {
            return;
        }
        scoringClient.submitScore(currentScore);
        lastReportedScore = currentScore;
    }

    private void refreshHighScore() {
        scoringClient.fetchHighScore();
    }

    private Entity findPlayer() {
        for (Entity entity : world.getEntities()) {
            if ("player".equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    private boolean hasPlayer() {
        return findPlayer() != null;
    }

    private void startGameSession() {
        resetWorld();
        gameData.resetRunState();
        refreshHighScore();
        lastReportedScore = 0;
        gameRunning = true;
        gameOver = false;
        hideOverlay();

        for (IGamePluginService plugin : gamePlugins) {
            plugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            addPolygon(entity);
        }
        updateHud();
    }

    private void resetWorld() {
        stopPlugins();
        for (Polygon polygon : new ArrayList<>(polygons.values())) {
            gameWindow.getChildren().remove(polygon);
        }
        polygons.clear();
        world.clearEntities();
    }

    private void stopPlugins() {
        for (IGamePluginService plugin : gamePlugins) {
            plugin.stop(gameData, world);
        }
    }

    private void addPolygon(Entity entity) {
        Polygon polygon = createPolygon(entity);
        polygons.put(entity, polygon);
        gameWindow.getChildren().add(polygon);
    }

    private Polygon createPolygon(Entity entity) {
        double[] coords = entity.getPolygonCoordinates();
        if (coords == null || coords.length == 0) {
            double radius = entity.getRadius() <= 0 ? 5 : entity.getRadius();
            coords = new double[] {radius, -radius, -radius, -radius, -radius, radius, radius, radius};
        }
        Polygon polygon = new Polygon(coords);
        applyColors(entity, polygon);
        return polygon;
    }

    private void applyColors(Entity entity, Polygon polygon) {
        String id = entity.getId();
        if ("player".equals(id)) {
            polygon.setFill(Color.YELLOW);
            polygon.setStroke(Color.YELLOW);
            return;
        }
        if ("enemy".equals(id)) {
            polygon.setFill(Color.RED);
            polygon.setStroke(Color.RED);
            return;
        }
        if (id != null && id.startsWith("asteroid")) {
            polygon.setFill(Color.BLACK);
            polygon.setStroke(Color.BLACK);
            return;
        }
        polygon.setFill(Color.WHITE);
        polygon.setStroke(Color.WHITE);
    }

    private void showStartOverlay() {
        overlayText.setText("AsteroidsFX");
        actionButton.setText("Start");
        overlayText.setVisible(true);
        actionButton.setVisible(true);
    }

    private void showResetOverlay() {
        overlayText.setText(gameOver ? "Game Over" : "Paused");
        actionButton.setText("Reset");
        overlayText.setVisible(true);
        actionButton.setVisible(true);
    }

    private void hideOverlay() {
        overlayText.setVisible(false);
        actionButton.setVisible(false);
    }
}
