// Module info for the Core module.
module Core {
requires javafx.graphics;
requires javafx.controls;
exports dk.sdu.cbse;
opens dk.sdu.cbse to javafx.graphics;
exports dk.sdu.cbse.spi;
exports dk.sdu.cbse.common.data;
exports dk.sdu.cbse.common.bullet;
uses dk.sdu.cbse.spi.IGamePluginService;
uses dk.sdu.cbse.spi.IEntityProcessorService;
uses dk.sdu.cbse.spi.IPostEntityProcessorService;
  
}
