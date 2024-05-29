package nl.marcmanning.avoidtheballs;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.components.Rendering;
import nl.marcmanning.avoidtheballs.utils.Vector2D;

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
            Vector2D pos = movement.getPosition();
            Vector2D vel = movement.getVelocity();
            float speed = (float) Math.sqrt(vel.getX() * vel.getX() + vel.getY() * vel.getY());
            rendering.setPosition(pos.getX() + interpolation * vel.getX(), pos.getY() + interpolation * vel.getY());
            rendering.setColor(convertVelocityToColor(speed, Constants.MAX_SPEED));
        }
    }

    public Color convertVelocityToColor(float speed, float maxSpeed) {
        speed = Math.max(0, Math.min(speed, maxSpeed));
        double factor = speed / maxSpeed;
        int green = (int) ((1 - factor) * 255);
        int red = (int) (factor * 255);
        return Color.rgb(red, green, 0);
    }
}
