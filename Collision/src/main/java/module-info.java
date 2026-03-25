module Collision {
    requires Core;
    provides dk.sdu.cbse.spi.IPostEntityProcessingService with dk.sdu.cbse.collision.CollisionControlSystem;
    exports dk.sdu.cbse.collision;
}
