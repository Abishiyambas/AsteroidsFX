package dk.sdu.cbse;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.spi.IEntityProcessingService;
import dk.sdu.cbse.spi.IPostEntityProcessingService;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();

    private ServiceLoader<IEntityProcessingService> processors;
    private ServiceLoader<IPostEntityProcessingService> postProcessors;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: blue;");
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

        ServiceLoader<IGamePlugin> gamePlugins = ServiceLoader.load(IGamePlugin.class);
        for (IGamePlugin plugin : gamePlugins) {
            plugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            addPolygon(entity);
        }

        stage.setScene(scene);
        stage.setTitle("ASTEROIDS");
        stage.show();

        processors = ServiceLoader.load(IEntityProcessingService.class);
        postProcessors = ServiceLoader.load(IPostEntityProcessingService.class);

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
                update();
                draw();
                gameData.getKeys().update();
            }
        };
        timer.start();
    }

    private void update() {
        for (IEntityProcessingService service : processors) {
            service.process(gameData, world);
        }
        for (IPostEntityProcessingService service : postProcessors) {
            service.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
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

    private void addPolygon(Entity entity) {
        Polygon polygon = createPolygon(entity);
        polygons.put(entity, polygon);
        gameWindow.getChildren().add(polygon);
    }

    private Polygon createPolygon(Entity entity) {
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
}
