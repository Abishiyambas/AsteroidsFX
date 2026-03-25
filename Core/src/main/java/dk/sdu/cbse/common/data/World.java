package dk.sdu.cbse.common.data;
import java.util.List;
import java.util.ArrayList;

public class World {
private final List<Entity> entities = new ArrayList<>();
public void addEntity(Entity entity) {
    entities.add(entity);
}
public void removeEntity(Entity entity) {
    entities.remove(entity);
}
public List<Entity> getEntities() {
    return entities;
}
}
