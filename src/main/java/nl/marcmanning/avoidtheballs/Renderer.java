package nl.marcmanning.avoidtheballs;

import javafx.scene.layout.Pane;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.components.Rendering;
import nl.marcmanning.avoidtheballs.extra.Constants;
import nl.marcmanning.avoidtheballs.extra.Entity;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.List;
import java.util.Set;

public class Renderer {
    private final EntitySpace entitySpace;
    private final Pane pane;

    public Renderer(EntitySpace entitySpace, Pane pane) {
        this.entitySpace = entitySpace;
        this.pane = pane;
    }

    public void display(double interpolation) {
        Set<Entity> targets = entitySpace.getEntitiesContaining(List.of(Movement.class, Rendering.class));
        for (Entity target : targets) {
            Rendering rendering = target.getComponent(Rendering.class);
            Movement movement = target.getComponent(Movement.class);
            RealVector pos = movement.getPosition();
            RealVector vel = movement.getVelocity();
            rendering.addToPane(pane);
            rendering.setPosition(pos.add(vel.mapMultiply(interpolation * Constants.TICK_DURATION)));
        }
    }
}
