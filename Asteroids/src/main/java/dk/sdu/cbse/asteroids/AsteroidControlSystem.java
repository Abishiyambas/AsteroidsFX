package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.spi.IEntityProcessingService;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;

public class AsteroidControlSystem implements IEntityProcessingService {
    
public void process(GameData gameData, World world) { 
    
    double deltaTime = gameData.getDelta();
    for (Entity asteroid : world.getEntities()) {
        if(asteroid.getId()==null) continue;
        if(!asteroid.getId().startsWith("asteroid")) continue;

        double newX = asteroid.getX() + asteroid.getDx() * deltaTime;
        double newY = asteroid.getY() + asteroid.getDy() * deltaTime;
        if (newX < 0) newX += gameData.getDisplayWidth();
        if (newX > gameData.getDisplayWidth()) newX -= gameData.getDisplayWidth();

        if (newY < 0) newY += gameData.getDisplayHeight();
        if (newY > gameData.getDisplayHeight()) newY -= gameData.getDisplayHeight();

        asteroid.setX(newX);
        asteroid.setY(newY);

        
}


}

}
