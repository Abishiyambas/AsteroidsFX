package dk.sdu.cbse.player;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import java.util.ArrayList;
import java.util.List;


public class PlayerPlugin implements IGamePlugin {
   
public void start(GameData gameData, World world) {
    
    Entity player = new Entity();

        // Set id
        player.setId("player");

        // Center of screen
        player.setX(gameData.getDisplayWidth() / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);

        // No movement initially
        player.setDx(0);
        player.setDy(0);

        // Radius (fixed or small range — here fixed)
        player.setRadius(10);

        // Add to world
        world.addEntity(player);

} 

public void stop(GameData gameData, World world) {

    List<Entity> toRemove = new ArrayList<>();
     for (Entity e : world.getEntities()) {
            if ("player".equals(e.getId())) {
                toRemove.add(e);
            }
        }
    for (Entity e : toRemove) {
        world.removeEntity(e);
    }
    
}
}



