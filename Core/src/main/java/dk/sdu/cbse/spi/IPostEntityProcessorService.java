package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Performs cross-component processing after the regular entity updates.
 */
public interface IPostEntityProcessorService {

    /**
     * Preconditions: all frame entity processors have completed.
     * Postconditions: cross-cutting logic such as collision handling is applied.
     */
    void process(GameData gameData, World world);
}
