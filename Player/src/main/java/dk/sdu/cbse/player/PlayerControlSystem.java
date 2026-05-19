package dk.sdu.cbse.player;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessorService;
import java.util.Optional;
import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessorService {
    
    public void process(GameData gameData, World world) {
        double delta = gameData.getDelta();
        double rotationSpeed = 180;
        double moveSpeed = 200;

        for (Entity player : world.getEntities(Player.class)) {
            // Turn left/right.
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - rotationSpeed * delta);
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + rotationSpeed * delta);
            }
            // Move forward.
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                double changeX = Math.cos(Math.toRadians(player.getRotation())) * moveSpeed * delta;
                double changeY = Math.sin(Math.toRadians(player.getRotation())) * moveSpeed * delta;
                player.setX(player.getX() + changeX);
                player.setY(player.getY() + changeY);
            }
            // Shoot once per key press.
            if (gameData.getKeys().isPressed(GameKeys.SPACE)) {
                findBulletSPI().ifPresent(
                        spi -> world.addEntity(spi.createBullet(player, gameData))
                );
            }

            // Keep player inside the screen.
            if (player.getX() < 0) {
                player.setX(1);
            }
            if (player.getX() > gameData.getDisplayWidth()) {
                player.setX(gameData.getDisplayWidth() - 1);
            }
            if (player.getY() < 0) {
                player.setY(1);
            }
            if (player.getY() > gameData.getDisplayHeight()) {
                player.setY(gameData.getDisplayHeight() - 1);
            }
        }
    }

    private Optional<BulletSPI> findBulletSPI() {
        return ServiceLoader.load(BulletSPI.class).findFirst();
    }
}
