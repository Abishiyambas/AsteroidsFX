package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.spi.IEntityProcessingService;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessingService {
    private final Random rng = new Random();
    private double spawnTimer = 0;
    private static final int MIN_ASTEROIDS = 5;
    private static final double SPAWN_INTERVAL = 1.5;

public void process(GameData gameData, World world) { 
    
    double deltaTime = gameData.getDelta();
    double speed = 50;
    spawnTimer += deltaTime;
    int asteroidCount = 0;
    List<Entity> toAdd = new ArrayList<>();

    for (Entity asteroid : world.getEntities()) {
        if (asteroid.getId() == null) continue;
        if (!asteroid.getId().startsWith("asteroid")) continue;
        asteroidCount++;
        double changeX = Math.cos(Math.toRadians(asteroid.getRotation())) * speed * deltaTime;
        double changeY = Math.sin(Math.toRadians(asteroid.getRotation())) * speed * deltaTime;

        double newX = asteroid.getX() + changeX;
        double newY = asteroid.getY() + changeY;
        if (newX < 0) newX += gameData.getDisplayWidth();
        if (newX > gameData.getDisplayWidth()) newX -= gameData.getDisplayWidth();

        if (newY < 0) newY += gameData.getDisplayHeight();
        if (newY > gameData.getDisplayHeight()) newY -= gameData.getDisplayHeight();

        asteroid.setX(newX);
        asteroid.setY(newY);
    }

    if (asteroidCount < MIN_ASTEROIDS && spawnTimer >= SPAWN_INTERVAL) {
        toAdd.add(createAsteroid(gameData));
        spawnTimer = 0;
    }

    for (Entity asteroid : toAdd) {
        world.addEntity(asteroid);
    }


}

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        double size = rng.nextInt(10) + 5;
        asteroid.setId("asteroid" + System.nanoTime());
        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setX(rng.nextDouble() * gameData.getDisplayWidth());
        asteroid.setY(rng.nextDouble() * gameData.getDisplayHeight());
        asteroid.setRadius(size);
        asteroid.setRotation(rng.nextInt(360));
        return asteroid;
    }

}
