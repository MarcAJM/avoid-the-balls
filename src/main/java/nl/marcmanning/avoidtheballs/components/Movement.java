package nl.marcmanning.avoidtheballs.components;

import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

public class Movement implements Component {
    private double[] position;
    private double[] velocity;

    public Movement(double x, double y) {
        this.position = new double[]{x, y};
        this.velocity = new double[]{0.0, 0.0};
    }

    public Movement(double x, double y, double velX, double velY) {
        this.position = new double[]{x, y};
        this.velocity = new double[]{velX, velY};
    }

    public Movement(RealVector pos, RealVector vel) {
        this.position = pos.toArray();
        this.velocity = vel.toArray();
    }

    public RealVector getPosition() {
        return new ArrayRealVector(position);
    }

    public void setPosition(RealVector position) {
        this.position = position.toArray();
    }

    public RealVector getVelocity() {
        return new ArrayRealVector(velocity);
    }

    public void setVelocity(RealVector velocity) {
        this.velocity = velocity.toArray();
    }
}
