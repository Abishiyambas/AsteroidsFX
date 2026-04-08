
module Enemy{
   requires Core;
    provides dk.sdu.cbse.spi.IGamePlugin with dk.sdu.cbse.enemy.EnemyPlugin;
    exports dk.sdu.cbse.enemy;  
     provides dk.sdu.cbse.spi.IEntityProcessingService with dk.sdu.cbse.enemy.EnemyControlSystem;        
     uses dk.sdu.cbse.common.bullet.BulletSPI;
}
