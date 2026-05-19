// Module info for Collision.
module Collision {
    requires Core;
    provides dk.sdu.cbse.spi.IPostEntityProcessorService with dk.sdu.cbse.collision.CollisionControlSystem;
    exports dk.sdu.cbse.collision;
}
