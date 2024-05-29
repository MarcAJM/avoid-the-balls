package nl.marcmanning.avoidtheballs.components;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Rendering implements Component {
    private final Circle circle;
    private Color color;
    private float radius;

    public Rendering(Color color, float radius, Pane pane) {
        this.color = color;
        this.radius = radius;
        this.circle = new Circle(radius);
        this.circle.setFill(color);
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

    public void setPosition(float x, float y) {
        Platform.runLater(() -> {
            this.circle.setLayoutX(x);
            this.circle.setLayoutY(y);
        });
    }
}
