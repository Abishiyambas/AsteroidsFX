
// Module info for Bullet.
module Bullet{
   requires Core;
    provides dk.sdu.cbse.spi.IGamePluginService with dk.sdu.cbse.bullet.BulletPlugin;
    exports dk.sdu.cbse.bullet;
    provides dk.sdu.cbse.spi.IEntityProcessorService with dk.sdu.cbse.bullet.BulletControlSystem;
    provides dk.sdu.cbse.common.bullet.BulletSPI with dk.sdu.cbse.bullet.BulletControlSystem;
}
