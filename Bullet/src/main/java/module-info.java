
module Bullet{
   requires Core;
    provides dk.sdu.cbse.spi.IGamePlugin with dk.sdu.cbse.bullet.BulletPlugin;
    exports dk.sdu.cbse.bullet;
    provides dk.sdu.cbse.spi.IEntityProcessingService with dk.sdu.cbse.bullet.BulletControlSystem;
}