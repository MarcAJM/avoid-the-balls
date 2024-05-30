package nl.marcmanning.avoidtheballs.components;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.commons.math4.legacy.linear.RealVector;

public class Rendering implements Component {
    private final Circle circle;
    private Color color;
    private float radius;

    public Rendering(float x, float y, float radius, Pane pane) {
        this.color = Color.BLACK;
        this.radius = radius;
        this.circle = new Circle(radius);
        this.circle.setFill(color);
        this.circle.setLayoutX(x);
        this.circle.setLayoutY(y);
        pane.getChildren().add(circle);
    }

    public Color getColor() { return color; }

    public void setColor(Color color) {
        Platform.runLater(() -> {
            this.circle.setFill(color);
            this.color = color;
        });
    }

    public float getRadius() { return radius; }

    public void setRadius(float radius) {
        Platform.runLater(() -> {
            this.circle.setRadius(radius);
            this.radius = radius;
        });
    }

    public void setPosition(RealVector position) {
        Platform.runLater(() -> {
            this.circle.setLayoutX(position.getEntry(0));
            this.circle.setLayoutY(position.getEntry(1));
        });
    }
}
