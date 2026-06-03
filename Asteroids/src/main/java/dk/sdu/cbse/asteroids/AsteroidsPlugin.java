package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IGamePluginService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidsPlugin implements IGamePluginService {
   
public void start(GameData gameData, World world) {

// Create a few asteroids at the start.
Random rng = new Random();
    int count = 8;
for (int i = 0; i < count; i++) {

Entity asteroid = new Asteroid();
asteroid.setId("asteroid" + i);
double size = rng.nextInt(10) + 5;
asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
asteroid.setX(rng.nextDouble() * gameData.getDisplayWidth());
asteroid.setY(rng.nextDouble() * gameData.getDisplayHeight());
asteroid.setRadius(size);
asteroid.setRotation(rng.nextInt(90));

world.addEntity(asteroid);
}

}

public void stop(GameData gameData, World world) {
    List<Entity> toRemove = new ArrayList<>();
    for (Entity entity : world.getEntities()) {
        if (entity.getId() != null && entity.getId().startsWith("asteroid")) {
            toRemove.add(entity);
        }
    }
    for (Entity entity : toRemove) {
        world.removeEntity(entity);
    }
}

}
