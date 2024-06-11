package nl.marcmanning.avoidtheballs;

import nl.marcmanning.avoidtheballs.components.Component;

import java.util.*;

public class EntitySpace {
    private final Map<Class<? extends Component>,Set<Entity>> components;

    public EntitySpace() {
        components = new HashMap<>();
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

    public void add(int id, Component... components) {
        Entity entity = new Entity(id, components);
        add(entity);
    }

    public void add(Entity entity) {
        for (Component component : entity.getComponents()) {
            this.components.putIfAbsent(component.getClass(), new HashSet<>());
            this.components.get(component.getClass()).add(entity);
        }
    }
}
