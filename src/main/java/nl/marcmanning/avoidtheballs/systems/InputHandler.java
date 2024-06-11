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

public class InputHandler extends System implements EventHandler<MouseEvent> {
    private volatile RealVector position;

    public InputHandler(EntitySpace entitySpace, Pane pane) {
        super(entitySpace);
        pane.setOnMouseMoved(this);
        Point point = MouseInfo.getPointerInfo().getLocation();
        position = new ArrayRealVector(new double[]{point.getX(), point.getY()});
    }

    @Override
    public void update() {
        Set<Entity> entities = entitySpace.getEntitiesContaining(List.of(Controller.class));
        for (Entity entity : entities) {
            if (entity.hasComponent(Movement.class) && position != null) {
                Movement movement = entity.getComponent(Movement.class);
                movement.setPosition(position);
            }
        }
    }

    @Override
    public void handle(MouseEvent event) {
        position = new ArrayRealVector(new double[]{event.getX(), event.getY()});
    }
}
