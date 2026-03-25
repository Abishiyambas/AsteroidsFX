package dk.sdu.cbse.bullet;
import dk.sdu.cbse.spi.IGamePlugin;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;

public class BulletPlugin implements IGamePlugin {
    public void start(GameData gameData, World world) {
        Entity bullet = new Entity();
        // Set id
        bullet.setId("bullet0");
        // Center of screen
        bullet.setX(gameData.getDisplayWidth() / 2.0);
        bullet.setY(gameData.getDisplayHeight() / 2.0);
        // No movement initially
        bullet.setDx(0);
        bullet.setDy(-200);
        // Radius (fixed or small range — here fixed)
        bullet.setRadius(4);
        // Add to world
        world.addEntity(bullet);   


    }

    public void stop(GameData gameData, World world) {
    }

}
