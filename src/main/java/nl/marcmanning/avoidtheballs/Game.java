package nl.marcmanning.avoidtheballs;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nl.marcmanning.avoidtheballs.components.Controller;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.components.Rendering;
import nl.marcmanning.avoidtheballs.systems.*;
import nl.marcmanning.avoidtheballs.systems.System;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private volatile boolean running;
    private final EntitySpace entitySpace;
    private final Renderer renderer;
    private final Set<System> systems;

    public Game(Pane pane) {
        this.running = false;
        this.entitySpace = new EntitySpace();
        this.renderer = new Renderer(entitySpace, pane);
        this.systems = new HashSet<>();
        init(pane);
    }

    private void init(Pane pane) {
        systems.add(new MovementSystem(entitySpace));
        systems.add(new CollisionSystem(entitySpace, this));
        systems.add(new InputHandler(entitySpace, pane));
        Movement movement1 = new Movement(0, 0);
        Hitbox hitbox1 = new Hitbox(1);
        Controller controller1 = new Controller();
        Movement movement2 = new Movement(100, 100, 100, 100);
        Hitbox hitbox2 = new Hitbox(50);
        Rendering rendering2 = new Rendering(100, 100, 50, Color.BLACK);
        entitySpace.addEntity(movement1, hitbox1, controller1);
        entitySpace.addEntity(movement2, hitbox2, rendering2);
    }

    public void start() {
        if (!running) {
            running = true;
            new Thread(this::run).start();
        }
    }

    public void terminate() {
        running = false;
    }

    private void run() {
        double previous = getTimeMillis();
        double lag = 0.0;
        while (running) {
            double current = getTimeMillis();
            double elapsed = current - previous;
            previous = current;
            lag += elapsed;
            while (lag >= Constants.TICK_DURATION_MILLIS) {
                update();
                lag -= Constants.TICK_DURATION_MILLIS;
            }
            if (elapsed >= 1) {
                renderer.display(lag / Constants.TICK_DURATION_MILLIS);
            }
        }
    }

    private void update() {
        for (System system : systems) {
            system.update();
        }
    }

    private void applyLabelStyling(Label label, Pane pane) {
        label.setStyle("-fx-font-size: 32px; -fx-font-family: 'Verdana'; -fx-text-fill: black;");
        label.layoutXProperty().bind(pane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(pane.heightProperty().subtract(label.heightProperty()).divide(2));
    }

    public static long getTimeMillis() {
        return java.lang.System.nanoTime() / 1_000_000;
    }
}
