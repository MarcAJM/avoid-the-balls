package nl.marcmanning.avoidtheballs.systems;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nl.marcmanning.avoidtheballs.extra.Entity;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.components.Controller;
import nl.marcmanning.avoidtheballs.components.Movement;
import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class InputHandler extends System {

    public InputHandler(EntitySpace entitySpace, Pane pane) {
        super(entitySpace);
    }

    @Override
    public void update() {
        Set<Entity> entities = entitySpace.getEntitiesContaining(List.of(Controller.class));
        RealVector position = getMousePosition();
        for (Entity entity : entities) {
            if (entity.hasComponent(Movement.class)) {
                Movement movement = entity.getComponent(Movement.class);
                movement.setPosition(position);
            }
        }
    }

    private RealVector getMousePosition() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        return new ArrayRealVector(new double[]{point.getX(), point.getY()});
    }
}
