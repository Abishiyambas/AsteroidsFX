module Asteroids {

requires Core;
provides dk.sdu.cbse.spi.IGamePlugin with dk.sdu.cbse.asteroids.AsteroidsPlugin;
provides dk.sdu.cbse.spi.IEntityProcessingService with dk.sdu.cbse.asteroids.AsteroidControlSystem;
exports dk.sdu.cbse.asteroids;

    

}
