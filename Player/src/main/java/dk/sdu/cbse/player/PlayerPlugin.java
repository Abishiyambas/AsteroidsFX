package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePluginService;
import java.util.ArrayList;
import java.util.List;

public class PlayerPlugin implements IGamePluginService {
   
public void start(GameData gameData, World world) {
    // Create the player when the game starts.
    Entity player = new Player();

        // Set id
        player.setId("player");

        // Center of screen
        player.setX(gameData.getDisplayWidth() / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);

        // No movement initially
        player.setDx(0);
        player.setDy(0);

        // Shape and radius
        player.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        player.setRadius(8);
        // Health (hits to destroy)
        player.setHealth(gameData.getMaxPlayerHealth());
        // Owner id for friendly-fire checks
        player.setOwnerId("player");

        // Add to world
        world.addEntity(player);

} 

public void stop(GameData gameData, World world) {
    // Remove the player when the game stops.
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
