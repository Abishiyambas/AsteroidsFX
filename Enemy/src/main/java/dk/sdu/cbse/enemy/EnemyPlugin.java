package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePluginService;
import java.util.ArrayList;
import java.util.List;

public class EnemyPlugin implements IGamePluginService {
    public void start(GameData gameData, World world) {
        Entity enemy = new Enemy();
        enemy.setId("enemy");
        enemy.setX(gameData.getDisplayWidth() / 2.0);
        enemy.setY(gameData.getDisplayHeight() / 2.0);
        enemy.setDx(0);
        enemy.setDy(0);
        enemy.setPolygonCoordinates(-6, -6, 12, 0, -6, 6);
        enemy.setRadius(8);
        enemy.setRotation(90);
        enemy.setHealth(3);
        enemy.setOwnerId("enemy");
        world.addEntity(enemy);

    }

    public void stop(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            if ("enemy".equals(entity.getId()) || "enemy".equals(entity.getOwnerId())) {
                toRemove.add(entity);
            }
        }
        for (Entity entity : toRemove) {
            world.removeEntity(entity);
        }
    }
}
