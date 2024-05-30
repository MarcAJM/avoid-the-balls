package nl.marcmanning.avoidtheballs.systems;

import nl.marcmanning.avoidtheballs.Constants;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Movement;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.List;

public class MovementSystem extends System {

    public MovementSystem(EntitySpace entitySpace) {
        super(entitySpace);
    }

    @Override
    public void update() {
        List<Movement> targets = entitySpace.getComponentsOfType(Movement.class);
        for (Movement target : targets) {
            RealVector pos = target.getPosition();
            RealVector vel = target.getVelocity();
            target.setPosition(pos.add(vel.mapMultiply(Constants.TICK_DURATION)));
        }
    }
}
