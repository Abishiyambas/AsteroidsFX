package dk.sdu.cbse.asteroids;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsteroidControlSystemTest {

    @Test
    void processWrapsAsteroidHorizontallyAndVertically() {
        GameData gameData = new GameData();
        gameData.setDisplayWidth(100);
        gameData.setDisplayHeight(100);
        gameData.setDelta(1.0);

        Entity asteroid = new Asteroid();
        asteroid.setId("asteroid-test");
        asteroid.setX(99);
        asteroid.setY(99);
        asteroid.setRotation(45);

        World world = new World();
        world.addEntity(asteroid);

        new AsteroidControlSystem().process(gameData, world);

        assertTrue(asteroid.getX() >= 0 && asteroid.getX() <= 100);
        assertTrue(asteroid.getY() >= 0 && asteroid.getY() <= 100);
    }

    @Test
    void processSpawnsAsteroidWhenLevelNeedsMoreAndIntervalHasPassed() {
        GameData gameData = new GameData();
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        gameData.setDelta(2.0);

        World world = new World();

        new AsteroidControlSystem().process(gameData, world);

        assertEquals(1, world.getEntities().size());
        assertTrue(world.getEntities().get(0).getId().startsWith("asteroid"));
    }

    @Test
    void processDoesNotSpawnNewAsteroidsAfterGameIsWon() {
        GameData gameData = new GameData();
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        gameData.setDelta(2.0);
        for (int i = 0; i < 30; i++) {
            gameData.registerAsteroidDestroyed();
        }

        World world = new World();

        new AsteroidControlSystem().process(gameData, world);

        assertTrue(world.getEntities().isEmpty());
    }
}
