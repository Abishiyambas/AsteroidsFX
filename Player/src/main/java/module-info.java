// Module info for Player.
module Player {

requires Core;
provides dk.sdu.cbse.spi.IGamePluginService with dk.sdu.cbse.player.PlayerPlugin;
provides dk.sdu.cbse.spi.IEntityProcessorService with dk.sdu.cbse.player.PlayerControlSystem;
uses dk.sdu.cbse.common.bullet.BulletSPI;
exports dk.sdu.cbse.player;
}
