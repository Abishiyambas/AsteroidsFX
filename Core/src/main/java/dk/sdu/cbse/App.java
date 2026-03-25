package dk.sdu.cbse;
import dk.sdu.cbse.spi.IGamePlugin;

import java.util.ServiceLoader;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.spi.IEntityProcessingService;
import dk.sdu.cbse.spi.IPostEntityProcessingService;


public class App {

    
    
    public static void main(String[] args) {

        GameData gameData = new GameData();
        World world = new World();

        gameData.setDisplayWidth(800);
gameData.setDisplayHeight(600);
gameData.setDelta(0.016);

        ServiceLoader<IGamePlugin>gamePlugins = ServiceLoader.load(IGamePlugin.class);
for (IGamePlugin plugin : gamePlugins) {
    plugin.start(gameData, world);
    }


    ServiceLoader<IEntityProcessingService>processors = ServiceLoader.load(IEntityProcessingService.class);
for (IEntityProcessingService service : processors) {
    service.process(gameData, world);
    }

    ServiceLoader<IPostEntityProcessingService>postProcessors = ServiceLoader.load(IPostEntityProcessingService.class);
for (IPostEntityProcessingService service : postProcessors) {
    service.process(gameData, world);
}



    }
}
