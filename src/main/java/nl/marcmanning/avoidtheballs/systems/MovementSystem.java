package nl.marcmanning.avoidtheballs.systems;

import nl.marcmanning.avoidtheballs.Constants;
import nl.marcmanning.avoidtheballs.Entity;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Movement;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.List;
import java.util.Set;

public class MovementSystem extends System {

    public MovementSystem(EntitySpace entitySpace) {
        super(entitySpace);
    }

    @Override
    public void update() {
        Set<Entity> targets = entitySpace.getEntitiesContaining(List.of(Movement.class));
        for (Entity target : targets) {
            Movement movement = target.getComponent(Movement.class);
            RealVector pos = movement.getPosition();
            RealVector vel = movement.getVelocity();
            movement.setPosition(pos.add(vel.mapMultiply(Constants.TICK_DURATION)));
        }
    }
}
