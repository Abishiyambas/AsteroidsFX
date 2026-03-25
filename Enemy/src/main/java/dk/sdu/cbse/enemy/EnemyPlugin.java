package dk.sdu.cbse.enemy;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import java.util.ArrayList;
import java.util.List;


public class EnemyPlugin implements IGamePlugin {
    public void start(GameData gameData, World world) {

         
    Entity enemy = new Entity();

        // Set id
        enemy.setId("enemy");

        // Center of screen
        enemy.setX(gameData.getDisplayWidth() / 2.0);
        enemy.setY(gameData.getDisplayHeight() / 2.0);

        // No movement initially
        enemy.setDx(0);
        enemy.setDy(0);

        // Radius (fixed or small range — here fixed)
        enemy.setRadius(10);

        // Add to world
        world.addEntity(enemy);
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
