package nl.marcmanning.avoidtheballs.components;

import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

public class Movement implements Component {
    private double[] position;
    private double[] velocity;

    public Movement(float x, float y) {
        this.position = new double[]{x, y};
        this.velocity = new double[]{0.0, 0.0};
    }

    public Movement(float x, float y, float velX, float velY) {
        this.position = new double[]{x, y};
        this.velocity = new double[]{velX, velY};
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
