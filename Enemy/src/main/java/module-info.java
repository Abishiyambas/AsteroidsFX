
// Module info for Enemy.
module Enemy{
   requires Core;
    provides dk.sdu.cbse.spi.IGamePluginService with dk.sdu.cbse.enemy.EnemyPlugin;
    exports dk.sdu.cbse.enemy;  
     provides dk.sdu.cbse.spi.IEntityProcessorService with dk.sdu.cbse.enemy.EnemyControlSystem;        
     uses dk.sdu.cbse.common.bullet.BulletSPI;
}
