package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Post-frame processing service for cleanup and collision handling.
 */
public interface IPostEntityProcessingService {
    /**
     * Preconditions: entity updates for the current frame are complete.
     * Postconditions: collisions are resolved and entities are cleaned up.
     */
    void process(GameData gameData, World world);
}
