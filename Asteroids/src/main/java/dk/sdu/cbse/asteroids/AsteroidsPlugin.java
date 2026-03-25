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

Entity asteroid = new Entity();
asteroid.setId("asteroid" + i);
asteroid.setX(rng.nextDouble() * gameData.getDisplayWidth());
asteroid.setY(rng.nextDouble() * gameData.getDisplayHeight());
asteroid.setDx((rng.nextDouble() - 0.5) * 2);
asteroid.setDy((rng.nextDouble() - 0.5) * 2);
asteroid.setRadius(10+rng.nextDouble() * 10);

world.addEntity(asteroid);
}

}

public void stop(GameData gameData, World world) {
    
}

}
