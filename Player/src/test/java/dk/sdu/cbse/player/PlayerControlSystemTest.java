package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerControlSystemTest {

    @Test
    void processMovesPlayerForwardInFacingDirection() {
        GameData gameData = new GameData();
        gameData.setDelta(0.5);
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        gameData.getKeys().setKey(GameKeys.UP, true);

        Player player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0);

        World world = new World();
        world.addEntity(player);

        new PlayerControlSystem().process(gameData, world);

        assertEquals(200.0, player.getX(), 0.001);
        assertEquals(100.0, player.getY(), 0.001);
        assertTrue(player.getX() <= gameData.getDisplayWidth());
    }
}
