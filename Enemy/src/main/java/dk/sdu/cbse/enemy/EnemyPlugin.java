package dk.sdu.cbse.enemy;

import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import java.util.ArrayList;
import java.util.List;

public class EnemyPlugin implements IGamePlugin {
    public void start(GameData gameData, World world) {

    Entity enemy = new Enemy();

        // Set id
        enemy.setId("enemy");

        // Center of screen
        enemy.setX(gameData.getDisplayWidth() / 2.0);
        enemy.setY(gameData.getDisplayHeight() / 2.0);

        // No movement initially
        enemy.setDx(0);
        enemy.setDy(0);

        // Shape and radius
        enemy.setPolygonCoordinates(-6, -6, 12, 0, -6, 6);
        enemy.setRadius(8);
        enemy.setRotation(90);
        // Health (hits to destroy)
        enemy.setHealth(3);
        // Owner id for friendly-fire checks
        enemy.setOwnerId("enemy");

        // Add to world
        world.addEntity(enemy);
        Entity bullet = new Entity();
        bullet.setId("bulletE0");
        bullet.setOwnerId("enemy");
        bullet.setX(enemy.getX());
        bullet.setY(enemy.getY());
        bullet.setDx(0);
        bullet.setDy(200); // nedad
        bullet.setRadius(4);
        world.addEntity(bullet);
    }

    public void stop(GameData gameData, World world) {

        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities()) {
            if ("enemy".equals(e.getId())) {
                toRemove.add(e);
            }
        }
        for (Entity e : toRemove) {
            world.removeEntity(e);
        }
    }

}
