package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IPostEntityProcessingService;
import java.util.ArrayList;
import java.util.List;

public class CollisionControlSystem implements IPostEntityProcessingService {


    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        List<Entity> toRemove = new ArrayList<>();
        List<Entity> toAdd = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            if (a.getId() == null) {
                continue;
            }
            for (int j = i + 1; j < entities.size(); j++) {
                Entity b = entities.get(j);
                if (b.getId() == null) {
                    continue;
                }
                if (!collides(a, b)) {
                    continue;
                }
                handleCollision(a, b, toRemove, toAdd);
            }
        }

        for (Entity entity : toRemove) {
            world.removeEntity(entity);
        }
        for (Entity entity : toAdd) {
            world.addEntity(entity);
        }
    }

    private boolean collides(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double radius = a.getRadius() + b.getRadius();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    private void handleCollision(Entity a, Entity b, List<Entity> toRemove, List<Entity> toAdd) {
        String aId = a.getId();
        String bId = b.getId();

        if (isBullet(aId) && isAsteroid(bId)) {
            handleBulletAsteroid(a, b, toRemove, toAdd);
            return;
        }
        if (isBullet(bId) && isAsteroid(aId)) {
            handleBulletAsteroid(b, a, toRemove, toAdd);
            return;
        }
        if (isShip(aId) && isAsteroid(bId)) {
            markRemove(a, toRemove);
            return;
        }
        if (isShip(bId) && isAsteroid(aId)) {
            markRemove(b, toRemove);
            return;
        }
        if (isShip(aId) && isBullet(bId)) {
            markRemove(a, toRemove);
            markRemove(b, toRemove);
            return;
        }
        if (isShip(bId) && isBullet(aId)) {
            markRemove(a, toRemove);
            markRemove(b, toRemove);
        }
    }

    private void handleBulletAsteroid(Entity bullet, Entity asteroid, List<Entity> toRemove, List<Entity> toAdd) {
        markRemove(bullet, toRemove);

        double radius = asteroid.getRadius();
        if (radius <= 8) {
            markRemove(asteroid, toRemove);
            return;
        }

        markRemove(asteroid, toRemove);

        double newRadius = radius / 2.0;
        Entity a1 = cloneAsteroid(asteroid, asteroid.getX() + newRadius, asteroid.getY(), newRadius);
        Entity a2 = cloneAsteroid(asteroid, asteroid.getX() - newRadius, asteroid.getY(), newRadius);
        a1.setId(asteroid.getId() + "_a");
        a2.setId(asteroid.getId() + "_b");

        double dx = asteroid.getDx();
        double dy = asteroid.getDy();
        a1.setDx(-dy);
        a1.setDy(dx);
        a2.setDx(dy);
        a2.setDy(-dx);

        toAdd.add(a1);
        toAdd.add(a2);
    }

    private Entity cloneAsteroid(Entity source, double x, double y, double radius) {
        Entity e = new Entity();
        e.setX(x);
        e.setY(y);
        e.setRadius(radius);
        e.setDx(source.getDx());
        e.setDy(source.getDy());
        e.setRotation(source.getRotation());
        return e;
    }

    private void markRemove(Entity entity, List<Entity> toRemove) {
        if (!toRemove.contains(entity)) {
            toRemove.add(entity);
        }
    }

    private boolean isAsteroid(String id) {
        return id.startsWith("asteroid");
    }

    private boolean isBullet(String id) {
        return id.startsWith("bullet");
    }

    private boolean isShip(String id) {
        return id.startsWith("player") || id.startsWith("enemy");
    }
}
