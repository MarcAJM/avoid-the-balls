package nl.marcmanning.avoidtheballs.systems;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.util.Pair;
import nl.marcmanning.CollisionUtils;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.*;

public class CollisionDetector extends System {

    public CollisionDetector(EntitySpace entitySpace) {
        super(entitySpace);
    }

    @Override
    public void update() {
        List<Pair<Movement, Hitbox>> targets = entitySpace.getComponentPairsOfTypes(Movement.class, Hitbox.class);
        List<List<Pair<Movement, Hitbox>>> borderCollisions = getBorderCollisions(targets, Screen.getPrimary().getVisualBounds());
        handleBorderCollisions(borderCollisions);
        Set<List<Pair<Movement, Hitbox>>> collisions = getCollisions(targets);
        handleCollisions(collisions);
    }

    private void handleBorderCollisions(List<List<Pair<Movement, Hitbox>>> collisions) {
        for (Pair<Movement, Hitbox> collision : collisions.getFirst()) {
            RealVector vel = collision.getKey().getVelocity();
            vel.setEntry(1, vel.getEntry(1) * -1.0f);
            collision.getKey().setVelocity(vel);
        }
        for (Pair<Movement, Hitbox> collision : collisions.getLast()) {
            RealVector vel = collision.getKey().getVelocity();
            vel.setEntry(0, vel.getEntry(0) * -1.0f);
            collision.getKey().setVelocity(vel);
        }
    }

    private void handleCollisions(Set<List<Pair<Movement, Hitbox>>> collisions) {
        for (List<Pair<Movement, Hitbox>> collision : collisions) {
            handleCollision(collision.getFirst().getKey(), collision.getFirst().getValue().getRadius(),
                    collision.getLast().getKey(), collision.getLast().getValue().getRadius());
        }
    }

    private void handleCollision(Movement mov1, float rad1, Movement mov2, float rad2) {
        double[][] rotation = {{0,-1},{1,0}};
        RealVector normal = mov2.getPosition().subtract(mov1.getPosition()).unitVector();
        RealVector tangent = new Array2DRowRealMatrix(rotation).operate(normal).unitVector();
        float v1n = (float) normal.dotProduct(mov1.getVelocity());
        float v1tf = (float) tangent.dotProduct(mov1.getVelocity());
        float v2n = (float) normal.dotProduct(mov2.getVelocity());
        float v2tf = (float) tangent.dotProduct(mov2.getVelocity());
        float v1nf = computeFinalNormalVelocity(v1n, rad1, v2n, rad2);
        float v2nf = computeFinalNormalVelocity(v2n, rad2, v1n, rad1);
        mov1.setVelocity(normal.mapMultiply(v1nf).add(tangent.mapMultiply(v1tf)));
        mov2.setVelocity(normal.mapMultiply(v2nf).add(tangent.mapMultiply(v2tf)));
    }

    private float computeFinalNormalVelocity(float v1n, float r1, float v2n, float r2) {
        if (r1 == 0 || r2 == 0) {
            return v1n;
        } else {
            return ((r1 * r1 - r2 * r2) * v1n + 2 * r2 * r2 * v2n) / (r1 * r1 + r2 * r2);
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
        RealVector pos = target.getKey().getPosition();
        float radius = target.getValue().getRadius();
        if (pos.getEntry(1) + radius >= bounds.getHeight() || pos.getEntry(1) - radius <= 0) {
            horizontal.add(target);
        }
        if (pos.getEntry(0) + radius >= bounds.getWidth() || pos.getEntry(0) - radius <= 0) {
            vertical.add(target);
        }
    }

    private Set<List<Pair<Movement, Hitbox>>> getCollisions(List<Pair<Movement, Hitbox>> targets) {
        return CollisionUtils.getCircleCollisions(targets, p -> p.getKey().getPosition().getEntry(0),
                p -> p.getKey().getPosition().getEntry(1), p -> p.getValue().getRadius());
    }
}
