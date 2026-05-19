// Module info for Asteroids.
module Asteroids {

requires Core;
provides dk.sdu.cbse.spi.IGamePluginService with dk.sdu.cbse.asteroids.AsteroidsPlugin;
provides dk.sdu.cbse.spi.IEntityProcessorService with dk.sdu.cbse.asteroids.AsteroidControlSystem;
exports dk.sdu.cbse.asteroids;
}
