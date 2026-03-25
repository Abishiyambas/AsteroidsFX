module Player {

requires Core;
provides dk.sdu.cbse.spi.IGamePlugin with dk.sdu.cbse.player.PlayerPlugin;
provides dk.sdu.cbse.spi.IEntityProcessingService with dk.sdu.cbse.player.PlayerControlSystem;
exports dk.sdu.cbse.player;

    

}
