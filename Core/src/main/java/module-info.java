module Core {
requires javafx.graphics;
exports dk.sdu.cbse;
opens dk.sdu.cbse to javafx.graphics;
exports dk.sdu.cbse.spi;
exports dk.sdu.cbse.common.data;
exports dk.sdu.cbse.common.bullet;
uses dk.sdu.cbse.spi.IGamePlugin;
uses dk.sdu.cbse.spi.IEntityProcessingService;
uses dk.sdu.cbse.spi.IPostEntityProcessingService;
  
}
