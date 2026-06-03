// Module info for the Core module.
module Core {
requires javafx.graphics;
requires javafx.controls;
requires spring.context;
requires spring.beans;
requires spring.core;
requires spring.web;
requires com.fasterxml.jackson.databind;
requires java.logging;
exports dk.sdu.cbse;
opens dk.sdu.cbse to javafx.graphics, spring.core, spring.beans, spring.context, com.fasterxml.jackson.databind;
exports dk.sdu.cbse.spi;
exports dk.sdu.cbse.common.data;
exports dk.sdu.cbse.common.bullet;
uses dk.sdu.cbse.spi.IGamePluginService;
uses dk.sdu.cbse.spi.IEntityProcessorService;
uses dk.sdu.cbse.spi.IPostEntityProcessorService;
  
}
