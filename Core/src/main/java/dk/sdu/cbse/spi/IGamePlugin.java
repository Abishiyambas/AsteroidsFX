package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Represents a game plugin that can be loaded and initialized by the game.
 */

@Deprecated
public interface IGamePlugin extends IGamePluginService {

    /**
     * Preconditions: game world exists, plugin not already started.
     * Postconditions: plugin's entities are created and registered.
     */
    // Start and add entities to the world.
    void start(GameData gameData, World world);

    /**
     * Preconditions: plugin is started.
     * Postconditions: plugin's entities are removed/cleaned up.
     */
    // Stop and remove entities from the world.
    void stop(GameData gameData, World world);
}
