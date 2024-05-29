package nl.marcmanning.avoidtheballs.systems;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.util.Pair;
import nl.marcmanning.CollisionUtils;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.utils.Vector2D;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class CollisionDetector extends System {

    public CollisionDetector(EntitySpace entitySpace) {
        super(entitySpace);
    }

    @Override
    public void update() {
        List<Pair<Movement, Hitbox>> targets = entitySpace.getComponentPairsOfTypes(Movement.class, Hitbox.class);
        List<List<Pair<Movement, Hitbox>>> borderCollisions = getBorderCollisions(targets, Screen.getPrimary().getBounds());
        handleBorderCollisions(borderCollisions);
        Set<List<Pair<Movement, Hitbox>>> collisions = getCollisions(targets);
        handleCollisions(collisions);
    }

    private void handleBorderCollisions(List<List<Pair<Movement, Hitbox>>> collisions) {
        for (Pair<Movement, Hitbox> collision : collisions.getFirst()) {
            collision.getKey().getVelocity().invertY();
        }
        for (Pair<Movement, Hitbox> collision : collisions.getLast()) {
            collision.getKey().getVelocity().invertX();
        }
    }

    private void handleCollisions(Set<List<Pair<Movement, Hitbox>>> collisions) {
        for (List<Pair<Movement, Hitbox>> collision : collisions) {
            handleCollision(collision.getFirst().getKey().getVelocity(), collision.getFirst().getValue().getRadius(),
                    collision.getLast().getKey().getVelocity(), collision.getLast().getValue().getRadius());
        }
    }

    private void handleCollision(Vector2D vel1, float radius1, Vector2D vel2, float radius2) {
        Vector2D newVel1 = computeVelocityAfterCollision(vel1, radius1, vel2, radius2);
        Vector2D newVel2 = computeVelocityAfterCollision(vel2, radius2, vel1, radius1);
        vel1.setX(newVel1.getX());
        vel1.setY(newVel1.getY());
        vel2.setX(newVel2.getX());
        vel2.setY(newVel2.getY());
    }

    private Vector2D computeVelocityAfterCollision(Vector2D targetVel, float targetRad, Vector2D collidingVel, float collisionRad) {
        float velX = computeVelocityAfterCollision(targetVel.getX(), targetRad, collidingVel.getX(), collisionRad);
        float velY = computeVelocityAfterCollision(targetVel.getY(), targetRad, collidingVel.getY(), collisionRad);
        return new Vector2D(velX, velY);
    }

    private float computeVelocityAfterCollision(float v1, float r1, float v2, float r2) {
        if (r1 == 0 || r2 == 0) {
            return 0;
        } else {
            return ((r1 * r1 - r2 * r2) * v1 + 2 * r2 * r2 * v2) / (r1 * r1 + r2 * r2);
        }
    }

    private List<List<Pair<Movement, Hitbox>>> getBorderCollisions(List<Pair<Movement, Hitbox>> targets, Rectangle2D bounds) {
        List<Pair<Movement, Hitbox>> horizontal = new ArrayList<>();
        List<Pair<Movement, Hitbox>> vertical = new ArrayList<>();
        for (Pair<Movement, Hitbox> target : targets) {
            recordBorderCollisions(horizontal, vertical, target, bounds);
        }
        return new ArrayList<>(List.of(horizontal, vertical));
    }

    private void recordBorderCollisions(List<Pair<Movement, Hitbox>> horizontal, List<Pair<Movement, Hitbox>> vertical,
                                        Pair<Movement, Hitbox> target, Rectangle2D bounds) {
        float x = target.getKey().getPosition().getX();
        float y = target.getKey().getPosition().getY();
        float radius = target.getValue().getRadius();
        if (y + radius <= bounds.getHeight() || y - radius >= 0) {
            horizontal.add(target);
        }
        if (x + radius <= bounds.getWidth() || x - radius >= 0) {
            vertical.add(target);
        }
    }

    private Set<List<Pair<Movement, Hitbox>>> getCollisions(List<Pair<Movement, Hitbox>> targets) {
        return CollisionUtils.getCircleCollisions(targets, p -> p.getKey().getPosition().getX(),
                p -> p.getKey().getPosition().getY(), p -> p.getValue().getRadius());
    }
}
