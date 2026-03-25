package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Per-frame processing service for updating game entities.
 */
public interface IEntityProcessingService {
    /**
     * Preconditions: game loop is running and relevant entities exist.
     * Postconditions: entities have been updated for the current frame.
     */
    void process(GameData gameData, World world);
}
