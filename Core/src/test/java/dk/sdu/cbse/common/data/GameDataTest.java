package dk.sdu.cbse.common.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameDataTest {

    @Test
    void resetRunStateClearsProgressAndInputState() {
        GameData gameData = new GameData();
        gameData.setDelta(0.25);
        gameData.getKeys().setKey(GameKeys.LEFT, true);
        gameData.registerAsteroidDestroyed();
        gameData.setHealthRegenTimer(1.2);

        gameData.resetRunState();

        assertEquals(0.0, gameData.getDelta(), 0.001);
        assertEquals(0, gameData.getScore());
        assertEquals(0, gameData.getDestroyedAsteroids());
        assertEquals(0.0, gameData.getHealthRegenTimer(), 0.001);
        assertFalse(gameData.getKeys().isDown(GameKeys.LEFT));
    }

    @Test
    void levelProgressionIsCappedAndWinStateActivatesAtThirtyAsteroids() {
        GameData gameData = new GameData();

        for (int i = 0; i < 35; i++) {
            gameData.registerAsteroidDestroyed();
        }

        assertEquals(3, gameData.getLevel());
        assertEquals(3500, gameData.getScore());
        assertTrue(gameData.isGameWon());
    }

    @Test
    void exposesStableHealthConfigurationForHudAndRegeneration() {
        GameData gameData = new GameData();

        assertEquals(3, gameData.getMaxPlayerHealth());
        assertEquals(1.5, gameData.getHealthRegenInterval(), 0.001);
        assertEquals(3, gameData.getMaxLevel());
    }
}
