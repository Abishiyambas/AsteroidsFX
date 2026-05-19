package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.spi.IEntityProcessorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessorService {
    // Random for spawning new asteroids.
    private final Random rng = new Random();
    // Time since last spawn.
    private double spawnTimer = 0;
    public void process(GameData gameData, World world) {
        double deltaTime = gameData.getDelta();
        int level = gameData.getLevel();
        double speed = getAsteroidSpeed(level);
        int minimumAsteroids = getMinimumAsteroids(level);
        double spawnInterval = getSpawnInterval(level);

        spawnTimer += deltaTime;
        int asteroidCount = 0;
        List<Entity> toAdd = new ArrayList<>();

        for (Entity asteroid : world.getEntities()) {
            if (asteroid.getId() == null || !asteroid.getId().startsWith("asteroid")) {
                continue;
            }
            asteroidCount++;

            double changeX = Math.cos(Math.toRadians(asteroid.getRotation())) * speed * deltaTime;
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation())) * speed * deltaTime;

            double newX = asteroid.getX() + changeX;
            double newY = asteroid.getY() + changeY;
            if (newX < 0) {
                newX += gameData.getDisplayWidth();
            }
            if (newX > gameData.getDisplayWidth()) {
                newX -= gameData.getDisplayWidth();
            }

            if (newY < 0) {
                newY += gameData.getDisplayHeight();
            }
            if (newY > gameData.getDisplayHeight()) {
                newY -= gameData.getDisplayHeight();
            }

            asteroid.setX(newX);
            asteroid.setY(newY);
        }

        if (!gameData.isGameWon() && asteroidCount < minimumAsteroids && spawnTimer >= spawnInterval) {
            toAdd.add(createAsteroid(gameData));
            spawnTimer = 0;
        }

        for (Entity asteroid : toAdd) {
            world.addEntity(asteroid);
        }
    }

    private double getAsteroidSpeed(int level) {
        switch (level) {
            case 2:
                return 72;
            case 3:
                return 96;
            default:
                return 42;
        }
    }

    private int getMinimumAsteroids(int level) {
        switch (level) {
            case 2:
                return 6;
            case 3:
                return 8;
            default:
                return 4;
        }
    }

    private double getSpawnInterval(int level) {
        switch (level) {
            case 2:
                return 1.15;
            case 3:
                return 0.75;
            default:
                return 1.7;
        }
    }

    private Entity createAsteroid(GameData gameData) {
        // Make a new asteroid with random size and position.
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
