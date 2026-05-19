package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePluginService;
import java.util.ArrayList;
import java.util.List;

public class BulletPlugin implements IGamePluginService {
    public void start(GameData gameData, World world) {
        // No bullets at start. Bullets are created through BulletSPI.
    }

    public void stop(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.getId() != null && entity.getId().startsWith("bullet")) {
                toRemove.add(entity);
            }
        }
        for (Entity entity : toRemove) {
            world.removeEntity(entity);
        }
    }
}
