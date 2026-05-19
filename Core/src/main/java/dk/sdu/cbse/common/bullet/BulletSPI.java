package dk.sdu.cbse.common.bullet;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;

public interface BulletSPI {
    // Create a bullet based on a shooter entity.
    Entity createBullet(Entity shooter, GameData gameData);
}
