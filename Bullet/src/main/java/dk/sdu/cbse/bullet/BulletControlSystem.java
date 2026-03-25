package dk.sdu.cbse.bullet;
import dk.sdu.cbse.spi.IEntityProcessingService;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

import java.util.ArrayList;
import java.util.List;

import dk.sdu.cbse.common.data.Entity;


public class BulletControlSystem implements IEntityProcessingService {

    public void process(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity bullet : world.getEntities()) {

            if(bullet.getId() == null) continue;
            if (!bullet.getId().startsWith("bullet")) continue;
            double newY = bullet.getY() + bullet.getDy() * gameData.getDelta();
            bullet.setY(newY);


            if(newY<0) {
                toRemove.add(bullet);
            }
            
    }
    for (Entity e : toRemove) {
                world.removeEntity(e);

            

        }
}

}
