package dk.sdu.cbse;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePluginService;
import dk.sdu.cbse.spi.IEntityProcessorService;
import dk.sdu.cbse.spi.IPostEntityProcessorService;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

public class App extends Application {

    // Shared game state.
    private final GameData gameData = new GameData();
    // All entities live here.
    private final World world = new World();
    // Shapes for drawing each entity.
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    // The JavaFX window content.
    private final Pane gameWindow = new Pane();
    private final Text hudText = new Text();
    private final Text statusText = new Text();
    private final Text healthLabel = new Text();
    private final Rectangle healthBarBackground = new Rectangle();
    private final Rectangle healthBarFill = new Rectangle();
    private final Text overlayText = new Text();
    private final Button actionButton = new Button("Start");

    // Game update services loaded with ServiceLoader.
    private List<IGamePluginService> gamePlugins;
    private List<IEntityProcessorService> processors;
    private List<IPostEntityProcessorService> postProcessors;
    private ModuleLayer pluginLayer;
    private boolean gameRunning;
    private boolean gameOver;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set game size.
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        // Create the window.
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: blue;");
        setupHud();
        Scene scene = new Scene(gameWindow);
        // Keyboard input.
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

        // Load optional plugin modules (ModuleLayer).
        pluginLayer = loadPluginLayer();
        gamePlugins = loadServices(IGamePluginService.class);
        processors = loadServices(IEntityProcessorService.class);
        postProcessors = loadServices(IPostEntityProcessorService.class);

        // Show the window.
        stage.setScene(scene);
        stage.setTitle("ASTEROIDS");
        stage.setOnCloseRequest(event -> stopPlugins());
        stage.show();
        showStartOverlay();

        // Main game loop.
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double delta = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                gameData.setDelta(delta);
                // Update game logic.
                update();
                // Draw all entities.
                draw();
                // Remember key state for one-shot actions.
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
        // Per-frame logic.
        for (IEntityProcessorService service : processors) {
            service.process(gameData, world);
        }
        // Collisions and cleanup.
        for (IPostEntityProcessorService service : postProcessors) {
            service.process(gameData, world);
        }
        regeneratePlayerHealth();
        if (!hasPlayer()) {
            gameRunning = false;
            gameOver = true;
            showResetOverlay();
        }
        updateHud();
    }

    @Override
    public void stop() {
        stopPlugins();
    }

    private void draw() {
        // Remove polygons for entities that are gone.
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        // Add or move polygons for current entities.
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

    private void addPolygon(Entity entity) {
        // Add a new shape for an entity.
        Polygon polygon = createPolygon(entity);
        polygons.put(entity, polygon);
        gameWindow.getChildren().add(polygon);
    }

    private Polygon createPolygon(Entity entity) {
        // Create a shape from polygon points (or fallback square).
        double[] coords = entity.getPolygonCoordinates();
        if (coords == null || coords.length == 0) {
            double r = entity.getRadius();
            if (r <= 0) {
                r = 5;
            }
            coords = new double[] {r, -r, -r, -r, -r, r, r, r};
        }
        Polygon polygon = new Polygon(coords);
        applyColors(entity, polygon);
        return polygon;
    }

    private void applyColors(Entity entity, Polygon polygon) {
        // Simple color rules by id.
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
        if (id != null && id.startsWith("healthPickup")) {
            polygon.setFill(Color.LIMEGREEN);
            polygon.setStroke(Color.WHITE);
            return;
        }
        polygon.setFill(Color.WHITE);
        polygon.setStroke(Color.WHITE);
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
        hudText.toFront();
        statusText.toFront();
        healthLabel.setText("Health");
        int currentHealth = getPlayerHealth();
        double ratio = currentHealth / (double) gameData.getMaxPlayerHealth();
        healthBarFill.setWidth(Math.max(0, 120 * ratio));
        healthLabel.toFront();
        healthBarBackground.toFront();
        healthBarFill.toFront();
        overlayText.toFront();
        actionButton.toFront();
    }

    private int getPlayerHealth() {
        for (Entity entity : world.getEntities()) {
            if ("player".equals(entity.getId())) {
                return Math.max(0, entity.getHealth());
            }
        }
        return 0;
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

    private ModuleLayer loadPluginLayer() {
        Path pluginsDir = Paths.get("plugins");
        if (!Files.isDirectory(pluginsDir)) {
            return null;
        }
        ModuleFinder finder = ModuleFinder.of(pluginsDir);
        Set<String> moduleNames = finder.findAll().stream()
                .map(ref -> ref.descriptor().name())
                .collect(Collectors.toSet());
        if (moduleNames.isEmpty()) {
            return null;
        }
        Configuration parentConfig = ModuleLayer.boot().configuration();
        Configuration config = parentConfig.resolve(finder, ModuleFinder.of(), moduleNames);
        return ModuleLayer.boot().defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());
    }

    private <T> List<T> loadServices(Class<T> service) {
        List<T> result = new ArrayList<>();
        ServiceLoader.load(service).forEach(result::add);
        if (pluginLayer != null) {
            ServiceLoader.load(pluginLayer, service).forEach(result::add);
        }
        return result;
    }

    private void stopPlugins() {
        if (gamePlugins == null) {
            return;
        }
        for (IGamePluginService plugin : gamePlugins) {
            plugin.stop(gameData, world);
        }
    }
}
