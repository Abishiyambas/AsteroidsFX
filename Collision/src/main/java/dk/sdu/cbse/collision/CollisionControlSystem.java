package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IPostEntityProcessorService;
import java.util.ArrayList;
import java.util.List;

public class CollisionControlSystem implements IPostEntityProcessorService {


    public void process(GameData gameData, World world) {
        // Copy entities so we can safely remove/add later.
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
                if (toRemove.contains(a) || toRemove.contains(b)) {
                    continue;
                }
                if (!collides(a, b)) {
                    continue;
                }
                // Handle the collision rules.
                handleCollision(gameData, a, b, toRemove, toAdd);
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
        // Pythagoras: distance between centers vs. sum of radii.
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double radius = a.getRadius() + b.getRadius();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    private void handleCollision(GameData gameData, Entity a, Entity b, List<Entity> toRemove, List<Entity> toAdd) {
        String aId = a.getId();
        String bId = b.getId();

        // Bullet hits asteroid.
        if (isBullet(aId) && isAsteroid(bId)) {
            handleBulletAsteroid(gameData, a, b, toRemove, toAdd);
            return;
        }
        if (isBullet(bId) && isAsteroid(aId)) {
            handleBulletAsteroid(gameData, b, a, toRemove, toAdd);
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
            handleShipBullet(a, b, toRemove);
            return;
        }
        if (isShip(bId) && isBullet(aId)) {
            handleShipBullet(b, a, toRemove);
        }
    }

    private void handleBulletAsteroid(GameData gameData, Entity bullet, Entity asteroid,
            List<Entity> toRemove, List<Entity> toAdd) {
        // Bullet is always removed.
        markRemove(bullet, toRemove);
        gameData.registerAsteroidDestroyed();

        double radius = asteroid.getRadius();
        if (radius <= 8) {
            // Small asteroids disappear.
            markRemove(asteroid, toRemove);
            return;
        }

        markRemove(asteroid, toRemove);

        double newRadius = radius / 2.0;
        Entity a1 = cloneAsteroid(asteroid, asteroid.getX() + newRadius, asteroid.getY(), newRadius);
        Entity a2 = cloneAsteroid(asteroid, asteroid.getX() - newRadius, asteroid.getY(), newRadius);
        a1.setId(asteroid.getId() + "_a");
        a2.setId(asteroid.getId() + "_b");

        a1.setRotation(asteroid.getRotation() + 30);
        a2.setRotation(asteroid.getRotation() - 30);

        toAdd.add(a1);
        toAdd.add(a2);
    }

    private Entity cloneAsteroid(Entity source, double x, double y, double radius) {
        // Copy an asteroid with smaller size.
        Entity e = new Entity();
        e.setX(x);
        e.setY(y);
        e.setRadius(radius);
        e.setRotation(source.getRotation());
        e.setPolygonCoordinates(radius, -radius, -radius, -radius, -radius, radius, radius, radius);
        return e;
    }

    private void markRemove(Entity entity, List<Entity> toRemove) {
        // Avoid duplicates in remove list.
        if (!toRemove.contains(entity)) {
            toRemove.add(entity);
        }
    }

    private void handleShipBullet(Entity ship, Entity bullet, List<Entity> toRemove) {
        // Ignore friendly fire (but remove the bullet).
        if (isFriendlyFire(ship, bullet)) {
            markRemove(bullet, toRemove);
            return;
        }
        // Enemy bullet hits ship.
        markRemove(bullet, toRemove);
        int hp = ship.getHealth();
        hp = hp - 1;
        ship.setHealth(hp);
        if (hp <= 0) {
            markRemove(ship, toRemove);
        }
    }

    private boolean isFriendlyFire(Entity ship, Entity bullet) {
        // Same owner means friendly fire.
        String ownerId = bullet.getOwnerId();
        return ownerId != null && !ownerId.isEmpty() && ownerId.equals(ship.getId());
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

    private boolean isPlayer(String id) {
        return id.startsWith("player");
    }
}
