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

public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
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
