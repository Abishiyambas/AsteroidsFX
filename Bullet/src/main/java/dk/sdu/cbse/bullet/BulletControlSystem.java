package dk.sdu.cbse.bullet;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessingService;
import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    public void process(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        double delta = gameData.getDelta();
        double speed = 300;

        for (Entity bullet : world.getEntities(Bullet.class)) {
            double changeX = Math.cos(Math.toRadians(bullet.getRotation())) * speed * delta;
            double changeY = Math.sin(Math.toRadians(bullet.getRotation())) * speed * delta;
            bullet.setX(bullet.getX() + changeX);
            bullet.setY(bullet.getY() + changeY);

            if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                    || bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight()) {
                toRemove.add(bullet);
            }
        }
        for (Entity e : toRemove) {
            world.removeEntity(e);
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setId("bullet" + System.nanoTime());
        bullet.setOwnerId(shooter.getId());
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        double changeX = Math.cos(Math.toRadians(shooter.getRotation()));
        double changeY = Math.sin(Math.toRadians(shooter.getRotation()));
        bullet.setX(shooter.getX() + changeX * 10);
        bullet.setY(shooter.getY() + changeY * 10);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(2);
        return bullet;
    }
}
