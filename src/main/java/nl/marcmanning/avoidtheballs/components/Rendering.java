package nl.marcmanning.avoidtheballs.components;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.commons.math4.legacy.linear.RealVector;

public class Rendering implements Component {
    private Circle circle;
    private Color color;
    private double radius;
    private boolean isInPane;

    public Rendering(double x, double y, double radius, Color color) {
        this.color = color;
        this.radius = radius;
        this.isInPane = false;
        Platform.runLater(() -> {
            this.circle = new Circle(radius);
            this.circle.setFill(color);
            this.circle.setLayoutX(x);
            this.circle.setLayoutY(y);
        });
    }

    public Color getColor() { return color; }

    public void setColor(Color color) {
        Platform.runLater(() -> {
            this.circle.setFill(color);
        });
        this.color = color;
    }

    public double getRadius() { return radius; }

    public void setRadius(double radius) {
        Platform.runLater(() -> {
            this.circle.setRadius(radius);
        });
        this.radius = radius;
    }

    public void setPosition(RealVector position) {
        Platform.runLater(() -> {
            this.circle.setLayoutX(position.getEntry(0));
            this.circle.setLayoutY(position.getEntry(1));
        });
    }

    public void addToPane(Pane pane) {
        if (!isInPane) {
            Platform.runLater(() -> {
                pane.getChildren().add(circle);
            });
            isInPane = true;
        }
    }
}
