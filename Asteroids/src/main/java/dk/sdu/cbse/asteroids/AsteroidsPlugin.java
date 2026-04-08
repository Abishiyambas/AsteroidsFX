package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import java.util.Random;
import dk.sdu.cbse.common.data.Entity;

public class AsteroidsPlugin implements IGamePlugin {
   
public void start(GameData gameData, World world) {

Random rng = new Random();
int count = 5;
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
    
}

}
