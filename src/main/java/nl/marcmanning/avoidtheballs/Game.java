package nl.marcmanning.avoidtheballs;

import javafx.scene.layout.Pane;
import nl.marcmanning.avoidtheballs.components.Controller;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.systems.*;
import nl.marcmanning.avoidtheballs.systems.System;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private volatile boolean running;
    private final EntitySpace entitySpace;
    private final Renderer renderer;
    private final Set<System> systems;
    private final Listener listener;
    private final long startingTime;

    public Game(Pane pane, Listener listener) {
        this.running = false;
        this.entitySpace = new EntitySpace();
        this.renderer = new Renderer(entitySpace, pane);
        this.systems = new HashSet<>();
        this.listener = listener;
        this.startingTime = getTimeMillis();
        init(pane);
    }

    private void init(Pane pane) {
        pane.setStyle("-fx-background-color: black;");
        systems.add(new MovementSystem(entitySpace));
        systems.add(new CollisionSystem(entitySpace, this));
        systems.add(new InputHandler(entitySpace, pane));
        systems.add(new Spawner(entitySpace, startingTime));
        entitySpace.add(0, new Movement(0, 0), new Hitbox(1), new Controller());
    }

    public void start() {
        if (!running) {
            running = true;
            new Thread(this::run).start();
        }
    }

    public void stop() {
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

    public Listener getListener() {
        return listener;
    }

    public long getCurrentScore() {
        return (getTimeMillis() - startingTime) / 100;
    }

    public static long getTimeMillis() {
        return java.lang.System.nanoTime() / 1_000_000;
    }
}
