package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Post-frame processing service for cleanup and collision handling.
 */
@Deprecated
public interface IPostEntityProcessingService extends IPostEntityProcessorService {
    /**
     * Preconditions: entity updates for the current frame are complete.
     * Postconditions: collisions are resolved and entities are cleaned up.
     */
    // Handle collisions and cleanup after updates.
    void process(GameData gameData, World world);
}
