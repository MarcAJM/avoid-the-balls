package nl.marcmanning.avoidtheballs;

import nl.marcmanning.avoidtheballs.components.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Entity {
    private final int id;
    private final Map<Class<? extends Component>, Component> components;

    public Entity(int id, Component... components) {
        this.id = id;
        this.components = new HashMap<>();
        for (Component component : components) {
            this.components.put(component.getClass(), component);
        }
    }

    public int getId() {
        return id;
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public Set<Component> getComponents() {
        return new HashSet<>(components.values());
    }
}
