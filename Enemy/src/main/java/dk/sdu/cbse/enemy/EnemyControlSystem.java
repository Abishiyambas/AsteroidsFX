package dk.sdu.cbse.enemy;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.spi.IEntityProcessingService;

public class EnemyControlSystem implements IEntityProcessingService {
    
    public void process(GameData gameData, World world) {
        double delta = gameData.getDelta();
        for (Entity enemy : world.getEntities()) {
           if (enemy.getId() == null) continue;
     if (!enemy.getId().equals("enemy")) continue;
     
     double newY = enemy.getY() + 50 * delta; 
        if (newY > gameData.getDisplayHeight()) newY = 0;
            enemy.setY(newY);

        }

    }
}
