package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessorService {
    private double shootTimer = 0;
    private double directionTimer = 0;
    private double horizontalDirection = 1;

    public void process(GameData gameData, World world) {
        List<Entity> bulletsToAdd = new ArrayList<>();
        double delta = gameData.getDelta();
        int level = gameData.getLevel();
        Entity player = findPlayer(world);

        shootTimer += delta;
        directionTimer += delta;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            updateMovement(gameData, enemy, player, delta, level);
            aimAtPlayer(enemy, player);

            if (player != null && shootTimer >= getShootInterval(level)) {
                findBulletSPI().ifPresent(spi -> bulletsToAdd.add(spi.createBullet(enemy, gameData)));
                shootTimer = 0;
            }
        }

        for (Entity bullet : bulletsToAdd) {
            world.addEntity(bullet);
        }
    }

    private void updateMovement(GameData gameData, Entity enemy, Entity player, double delta, int level) {
        double verticalSpeed = getVerticalSpeed(level);
        double horizontalSpeed = getHorizontalSpeed(level);

        if (directionTimer >= getDirectionInterval(level)) {
            directionTimer = 0;
            horizontalDirection = -horizontalDirection;
            if (player != null) {
                horizontalDirection = player.getX() >= enemy.getX() ? 1 : -1;
            }
        }

        double newX = enemy.getX() + horizontalDirection * horizontalSpeed * delta;
        double newY = enemy.getY() + verticalSpeed * delta;

        if (newX < 20) {
            newX = 20;
            horizontalDirection = 1;
        }
        if (newX > gameData.getDisplayWidth() - 20) {
            newX = gameData.getDisplayWidth() - 20;
            horizontalDirection = -1;
        }
        if (newY > gameData.getDisplayHeight() * 0.45) {
            newY = 40;
        }

        enemy.setX(newX);
        enemy.setY(newY);
    }

    private void aimAtPlayer(Entity enemy, Entity player) {
        if (player == null) {
            return;
        }
        double dx = player.getX() - enemy.getX();
        double dy = player.getY() - enemy.getY();
        enemy.setRotation(Math.toDegrees(Math.atan2(dy, dx)));
    }

    private Entity findPlayer(World world) {
        for (Entity entity : world.getEntities()) {
            if ("player".equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    private double getShootInterval(int level) {
        switch (level) {
            case 2:
                return 1.4;
            case 3:
                return 0.95;
            default:
                return 2.1;
        }
    }

    private double getVerticalSpeed(int level) {
        switch (level) {
            case 2:
                return 60;
            case 3:
                return 80;
            default:
                return 45;
        }
    }

    private double getHorizontalSpeed(int level) {
        switch (level) {
            case 2:
                return 120;
            case 3:
                return 170;
            default:
                return 90;
        }
    }

    private double getDirectionInterval(int level) {
        switch (level) {
            case 2:
                return 1.0;
            case 3:
                return 0.65;
            default:
                return 1.5;
        }
    }

    private Optional<BulletSPI> findBulletSPI() {
        return ServiceLoader.load(BulletSPI.class).findFirst();
    }
}
