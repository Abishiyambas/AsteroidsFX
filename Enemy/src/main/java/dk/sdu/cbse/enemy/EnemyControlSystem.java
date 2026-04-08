package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessingService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private double shootTimer = 0;

    public void process(GameData gameData, World world) {
        List<Entity> bulletsToAdd = new ArrayList<>();
        double delta = gameData.getDelta();
        shootTimer += delta;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            double newY = enemy.getY() + 50 * delta;
            if (newY > gameData.getDisplayHeight()) newY = 0;
            enemy.setY(newY);

            if (shootTimer >= 1.0) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> bulletsToAdd.add(spi.createBullet(enemy, gameData))
                );
                shootTimer = 0;
            }
        }

        for (Entity bullet : bulletsToAdd) {
            world.addEntity(bullet);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
