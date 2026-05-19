package dk.sdu.cbse.common.data;
import java.util.List;
import java.util.ArrayList;

public class World {
// All entities in the game.
private final List<Entity> entities = new ArrayList<>();
public void addEntity(Entity entity) {
    // Add a new entity.
    entities.add(entity);
}
public void removeEntity(Entity entity) {
    // Remove an entity.
    entities.remove(entity);
}
public List<Entity> getEntities() {
    // Get all entities.
    return entities;
}

public void clearEntities() {
    entities.clear();
}

public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
    // Get only entities of specific types (like Player or Bullet).
    List<Entity> result = new ArrayList<>();
    for (Entity e : entities) {
        for (Class<E> entityType : entityTypes) {
            if (entityType.equals(e.getClass())) {
                result.add(e);
            }
        }
    }
    return result;
}
}
