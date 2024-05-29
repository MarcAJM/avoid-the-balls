package nl.marcmanning.avoidtheballs.components;

public class Hitbox implements Component {
    private float radius;

    public Hitbox(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
