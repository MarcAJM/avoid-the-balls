package nl.marcmanning.avoidtheballs.components;

public class Hitbox implements Component {
    private double radius;

    public Hitbox(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
