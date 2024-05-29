package nl.marcmanning.avoidtheballs;

import javafx.scene.layout.Pane;

public class Renderer {
    private final EntitySpace entitySpace;
    private final Pane pane;

    public Renderer(EntitySpace entitySpace, Pane pane) {
        this.entitySpace = entitySpace;
        this.pane = pane;
    }

    public void display(float interpolation) {

    }
}
