package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionControlSystemTest {

    @Test
    void processSplitsLargeAsteroidWhenHitByBullet() {
        World world = new World();
        GameData gameData = new GameData();
        Entity bullet = new Entity();
        bullet.setId("bullet-1");
        bullet.setOwnerId("player");
        bullet.setX(50);
        bullet.setY(50);
        bullet.setRadius(1);

        Entity asteroid = new Entity();
        asteroid.setId("asteroid-1");
        asteroid.setX(50);
        asteroid.setY(50);
        asteroid.setRadius(12);
        asteroid.setRotation(90);

        world.addEntity(bullet);
        world.addEntity(asteroid);

        new CollisionControlSystem().process(gameData, world);

        assertFalse(world.getEntities().contains(bullet));
        assertFalse(world.getEntities().contains(asteroid));
        assertEquals(100, gameData.getScore());
        assertEquals(1, gameData.getDestroyedAsteroids());

        List<Entity> asteroids = world.getEntities().stream()
                .filter(entity -> entity.getId() != null && entity.getId().startsWith("asteroid-1_"))
                .toList();
        assertEquals(2, asteroids.size());
        assertTrue(asteroids.stream().allMatch(entity -> entity.getRadius() == 6.0));
    }

    @Test
    void processDamagesShipOnEnemyBulletHit() {
        World world = new World();

        Entity player = new Entity();
        player.setId("player");
        player.setX(25);
        player.setY(25);
        player.setRadius(5);
        player.setHealth(2);

        Entity bullet = new Entity();
        bullet.setId("bullet-2");
        bullet.setOwnerId("enemy");
        bullet.setX(25);
        bullet.setY(25);
        bullet.setRadius(1);

        world.addEntity(player);
        world.addEntity(bullet);

        new CollisionControlSystem().process(new GameData(), world);

        assertEquals(1, player.getHealth());
        assertTrue(world.getEntities().contains(player));
        assertFalse(world.getEntities().contains(bullet));
    }

    @Test
    void gameDataAdvancesLevelsEveryTenAsteroids() {
        GameData gameData = new GameData();

        for (int i = 0; i < 10; i++) {
            gameData.registerAsteroidDestroyed();
        }
        assertEquals(2, gameData.getLevel());

        for (int i = 0; i < 10; i++) {
            gameData.registerAsteroidDestroyed();
        }
        assertEquals(3, gameData.getLevel());
        assertFalse(gameData.isGameWon());

        for (int i = 0; i < 10; i++) {
            gameData.registerAsteroidDestroyed();
        }
        assertEquals(3, gameData.getLevel());
        assertTrue(gameData.isGameWon());
        assertEquals(3000, gameData.getScore());
    }
}
