package nl.marcmanning.avoidtheballs.extra;

import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class CollisionUtils {

    public static <T> Set<Set<T>> getCircleCollisions(Set<T> elements, Function<T,RealVector> pos, ToDoubleFunction<T> radius) {
        Set<Set<T>> collisions = getCollisionsOnTwoAxes(
                elements, t -> pos.apply(t).mapSubtract(radius.applyAsDouble(t)), t -> pos.apply(t).mapAdd(radius.applyAsDouble(t)));
        Set<Set<T>> circleCollisions = new HashSet<>();
        for (Set<T> collision : collisions) {
            Iterator<T> iterator = collision.iterator();
            T t1 = iterator.next();
            T t2 = iterator.next();
            if (hasCircleCollision(pos.apply(t1), radius.applyAsDouble(t1), pos.apply(t2), radius.applyAsDouble(t2))) {
                circleCollisions.add(collision);
            }
        }
        return circleCollisions;
    }

    public static <T> Set<Set<T>> getCollisionsOnTwoAxes(Set<T> elements, Function<T, RealVector> min, Function<T, RealVector> max) {
        Set<Set<T>> collisions = getCollisionsOnSingleAxis(elements, t -> min.apply(t).getEntry(0), t -> max.apply(t).getEntry(0));
        collisions.removeIf(collision -> !hasCollisionOnAxis(collision, t -> min.apply(t).getEntry(1), t -> max.apply(t).getEntry(1)));
        return collisions;
    }

    public static <T> Set<Set<T>> getCollisionsOnSingleAxis(Set<T> elements, ToDoubleFunction<T> min, ToDoubleFunction<T> max) {
        Set<T> actives = new HashSet<>();
        Set<Set<T>> collisions = new HashSet<>();
        List<T> sortedElements = new ArrayList<>(elements);
        sortedElements.sort(Comparator.comparingDouble(min));
        for (T current : sortedElements) {
            actives.removeIf(active -> max.applyAsDouble(active) < min.applyAsDouble(current));
            recordCollisions(current, actives, min, max, collisions);
            actives.add(current);
        }
        return collisions;
    }

    private static <T> void recordCollisions(T current, Set<T> actives, ToDoubleFunction<T> min, ToDoubleFunction<T> max,
                                             Set<Set<T>> collisions) {
        for (T active : actives) {
            if (max.applyAsDouble(current) > min.applyAsDouble(active)) {
                Set<T> collision = new HashSet<>();
                collision.add(current);
                collision.add(active);
                collisions.add(collision);
            }
        }
    }

    public static <T> boolean hasCollisionOnAxis(Set<T> collision, ToDoubleFunction<T> min, ToDoubleFunction<T> max) {
        Iterator<T> iterator = collision.iterator();
        T t1 = iterator.next();
        T t2 = iterator.next();
        return max.applyAsDouble(t1) > min.applyAsDouble(t2)
                && max.applyAsDouble(t2) > min.applyAsDouble(t1);
    }

    public static boolean hasCircleCollision(RealVector pos1, double r1, RealVector pos2, double r2) {
        RealVector diff = pos2.subtract(pos1);
        return diff.getNorm() <= r1 + r2;
    }
}
