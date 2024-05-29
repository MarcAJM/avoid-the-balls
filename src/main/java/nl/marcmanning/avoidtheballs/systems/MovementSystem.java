package nl.marcmanning.avoidtheballs.systems;

import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.utils.Vector2D;

import java.util.List;

public class MovementSystem extends System {

    public MovementSystem(EntitySpace entitySpace) {
        super(entitySpace);
    }

    @Override
    public void update() {
        List<Movement> targets = entitySpace.getComponentsOfType(Movement.class);
        for (Movement target : targets) {
            Vector2D pos = target.getPosition();
            Vector2D velocity = target.getVelocity();
            pos.addX(velocity.getX());
            pos.addY(velocity.getY());
        }
    }
}
