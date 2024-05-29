package nl.marcmanning.avoidtheballs;

import javafx.util.Pair;
import nl.marcmanning.avoidtheballs.components.Component;

import java.util.*;

public class EntitySpace {
    private final Map<Class<? extends Component>,Map<Integer,Component>> components;

    public EntitySpace() {
        components = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponentsOfType(Class<T> componentClass) {
        return new ArrayList<>((Collection<T>) this.components.getOrDefault(componentClass, new HashMap<>()).values());
    }

    @SuppressWarnings("unchecked")
    public <K extends Component, V extends Component> List<Pair<K,V>> getComponentPairsOfTypes(
            Class<K> class1, Class<V> class2) {
        Map<Integer,K> map1 = (Map<Integer, K>) components.get(class1);
        Map<Integer,V> map2 = (Map<Integer, V>) components.get(class2);
        if (map1 == null || map2 == null) {
            return Collections.emptyList();
        } else {
            return getComponentPairs(map1, map2);
        }
    }

    public void addEntity(int id, Component... components) {
        for (Component component : components) {
            this.components.putIfAbsent(component.getClass(), new HashMap<>());
            this.components.get(component.getClass()).put(id, component);
        }
    }

    private <K extends Component, V extends Component> List<Pair<K,V>> getComponentPairs(
            Map<Integer,K> map1, Map<Integer,V> map2) {
        Set<Integer> candidates = new HashSet<>(map1.keySet());
        candidates.retainAll(map2.keySet());
        List<Pair<K,V>> result = new ArrayList<>();
        for (Integer candidate : candidates) {
            result.add(new Pair<>(map1.get(candidate), map2.get(candidate)));
        }
        return result;
    }
}
