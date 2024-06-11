package nl.marcmanning.avoidtheballs.systems;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.util.Pair;
import nl.marcmanning.avoidtheballs.Game;
import nl.marcmanning.avoidtheballs.components.Controller;
import nl.marcmanning.avoidtheballs.extra.CollisionUtils;
import nl.marcmanning.avoidtheballs.extra.Entity;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.*;

public class CollisionSystem extends System {
    private final Game game;

    public CollisionSystem(EntitySpace entitySpace, Game game) {
        super(entitySpace);
        this.game = game;
    }

    @Override
    public void update() {
        Set<Entity> targets = entitySpace.getEntitiesContaining(List.of(Movement.class, Hitbox.class));
        Pair<Set<Entity>,Set<Entity>> borderCollisions = getBorderCollisions(targets, Screen.getPrimary().getVisualBounds());
        handleBorderCollisions(borderCollisions);
        Set<Set<Entity>> collisions = getCollisions(targets);
        handleCollisions(collisions);
    }

    private Pair<Set<Entity>,Set<Entity>> getBorderCollisions(Set<Entity> targets, Rectangle2D bounds) {
        Set<Entity> horizontal = new HashSet<>();
        Set<Entity> vertical = new HashSet<>();
        for (Entity target : targets) {
            recordBorderCollisions(horizontal, vertical, target, bounds);
        }
        return new Pair<>(horizontal, vertical);
    }

    private void recordBorderCollisions(Set<Entity> horizontal, Set<Entity> vertical,
                                        Entity target, Rectangle2D bounds) {
        RealVector pos = target.getComponent(Movement.class).getPosition();
        double radius = target.getComponent(Hitbox.class).getRadius();
        if (isBorderColliding(pos, radius, bounds.getHeight(), 1)) {
            horizontal.add(target);
        }
        if (isBorderColliding(pos, radius, bounds.getWidth(), 0)) {
            vertical.add(target);
        }
    }

    private boolean isBorderColliding(RealVector pos, double radius, double bound, int entry) {
        return pos.getEntry(entry) + radius > bound || pos.getEntry(entry) - radius < 0;
    }

    private Set<Set<Entity>> getCollisions(Set<Entity> targets) {
        return CollisionUtils.getCircleCollisions(targets, p -> p.getComponent(Movement.class).getPosition(),
                p -> p.getComponent(Hitbox.class).getRadius());
    }

    public void handleBorderCollisions(Pair<Set<Entity>,Set<Entity>> collisions) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        handleBorderCollisions(collisions.getKey(), 1, bounds.getHeight());
        handleBorderCollisions(collisions.getValue(), 0, bounds.getWidth());
    }

    private void handleBorderCollisions(Set<Entity> collisions, int entry, double bound) {
        for (Entity entity : collisions) {
            Movement movement = entity.getComponent(Movement.class);
            RealVector pos = movement.getPosition();
            RealVector vel = movement.getVelocity();
            double radius = entity.getComponent(Hitbox.class).getRadius();
            if ((pos.getEntry(entry) - radius <= 0 && vel.getEntry(entry) < 0) || (pos.getEntry(entry) + radius >= bound && vel.getEntry(entry) > 0)) {
                vel.setEntry(entry, vel.getEntry(entry) * -1.0);
                movement.setVelocity(vel);
            }
        }
    }

    public void handleCollisions(Set<Set<Entity>> collisions) {
        for (Set<Entity> collision : collisions) {
            Iterator<Entity> iterator = collision.iterator();
            Entity first = iterator.next();
            Entity last = iterator.next();
            if (first.hasComponent(Controller.class) || last.hasComponent(Controller.class)) {
                Platform.runLater(() -> {
                    game.stop();
                    game.getListener().update(game.getCurrentScore());
                });
            } else {
                handleCollision(first.getComponent(Movement.class), first.getComponent(Hitbox.class).getRadius(),
                        last.getComponent(Movement.class), last.getComponent(Hitbox.class).getRadius());
            }
        }
    }

    private void handleCollision(Movement mov1, double rad1, Movement mov2, double rad2) {
        double[][] rotation = {{0,-1},{1,0}};
        RealVector normal = mov2.getPosition().subtract(mov1.getPosition()).unitVector();
        RealVector tangent = new Array2DRowRealMatrix(rotation).operate(normal).unitVector();
        RealVector v1 = decomposeVelocity(mov1.getVelocity(), normal, tangent);
        RealVector v2 = decomposeVelocity(mov2.getVelocity(), normal, tangent);
        double v1nf = computeFinalNormalVelocity(v1.getEntry(0), rad1, v2.getEntry(0), rad2);
        double v2nf = computeFinalNormalVelocity(v2.getEntry(0), rad2, v1.getEntry(0), rad1);
        mov1.setVelocity(normal.mapMultiply(v1nf).add(tangent.mapMultiply(v1.getEntry(1))));
        mov2.setVelocity(normal.mapMultiply(v2nf).add(tangent.mapMultiply(v2.getEntry(1))));
    }

    private RealVector decomposeVelocity(RealVector velocity, RealVector normal, RealVector tangent) {
        return new ArrayRealVector(new double[]{normal.dotProduct(velocity), tangent.dotProduct(velocity)});
    }

    private double computeFinalNormalVelocity(double v1n, double r1, double v2n, double r2) {
        if (r1 == 0 || r2 == 0) {
            return v1n;
        } else {
            return ((r1 * r1 - r2 * r2) * v1n + 2 * r2 * r2 * v2n) / (r1 * r1 + r2 * r2);
        }
    }
}
