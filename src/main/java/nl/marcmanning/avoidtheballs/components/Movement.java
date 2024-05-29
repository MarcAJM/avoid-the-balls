package nl.marcmanning.avoidtheballs.components;

import nl.marcmanning.avoidtheballs.utils.Vector2D;

public class Movement implements Component {
    private final Vector2D position;
    private final Vector2D velocity;

    public Movement(float x, float y) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
    }

    public Movement(float x, float y, float velX, float velY) {
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(velX, velY);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }
}
