package nl.marcmanning.avoidtheballs;

import javafx.util.Pair;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.components.Rendering;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.List;

public class Renderer {
    private final EntitySpace entitySpace;

    public Renderer(EntitySpace entitySpace) {
        this.entitySpace = entitySpace;
    }

    public void display(float interpolation) {
        List<Pair<Movement,Rendering>> targets = entitySpace.getComponentPairsOfTypes(Movement.class, Rendering.class);
        for (Pair<Movement,Rendering> pair : targets) {
            Rendering rendering = pair.getValue();
            Movement movement = pair.getKey();
            RealVector pos = movement.getPosition();
            RealVector vel = movement.getVelocity();
            rendering.setPosition(pos.add(vel.mapMultiply(interpolation * Constants.TICK_DURATION)));
        }
    }
}
