package nl.marcmanning.avoidtheballs;

import nl.marcmanning.avoidtheballs.components.Component;

import java.util.*;

public class EntitySpace {
    private final Map<Class<? extends Component>,Set<Entity>> components;
    private int idPointer;

    public EntitySpace() {
        components = new HashMap<>();
        idPointer = 0;
    }

    public Set<Entity> getEntitiesContaining(List<Class<? extends Component>> componentClasses) {
        components.putIfAbsent(componentClasses.getFirst(), new HashSet<>());
        Set<Entity> entities = new HashSet<>(components.get(componentClasses.getFirst()));
        for (int i = 1; i < componentClasses.size(); i++) {
            components.putIfAbsent(componentClasses.get(i), new HashSet<>());
            entities.retainAll(components.get(componentClasses.get(i)));
        }
        return entities;
    }

    public void addEntity(Component... components) {
        Entity entity = new Entity(idPointer, components);
        for (Component component : components) {
            this.components.putIfAbsent(component.getClass(), new HashSet<>());
            this.components.get(component.getClass()).add(entity);
        }
        idPointer++;
    }
}
