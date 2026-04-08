package dk.sdu.cbse.bullet;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;

public class BulletPlugin implements IGamePlugin {
    public void start(GameData gameData, World world) {
    }

    public void stop(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getId() != null && e.getId().startsWith("bullet")) {
                world.removeEntity(e);
            }
        }
    }

}
