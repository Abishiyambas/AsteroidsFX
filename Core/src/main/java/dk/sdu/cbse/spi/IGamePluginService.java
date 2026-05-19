package dk.sdu.cbse.spi;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Starts and stops a game component that contributes entities to the world.
 */
public interface IGamePluginService {

    /**
     * Preconditions: game data and world are initialized.
     * Postconditions: the plugin has added any initial entities it owns.
     */
    void start(GameData gameData, World world);

    /**
     * Preconditions: the plugin has been started.
     * Postconditions: the plugin has removed or cleaned up the entities it owns.
     */
    void stop(GameData gameData, World world);
}
