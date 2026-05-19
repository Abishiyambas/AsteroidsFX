package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Updates component-owned entities once per frame.
 */
public interface IEntityProcessorService {

    /**
     * Preconditions: a game tick is in progress and the world is available.
     * Postconditions: owned entities have been updated for the current frame.
     */
    void process(GameData gameData, World world);
}
