package nl.marcmanning.avoidtheballs;

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

    public long getStartingTime() {
        return startingTime;
    }

    public static long computeScore(long startingTime) {
        return (getTimeMillis() - startingTime) / 100;
    }

    public static long getTimeMillis() {
        return java.lang.System.nanoTime() / 1_000_000;
    }
}
