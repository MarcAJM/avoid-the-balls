package nl.marcmanning.avoidtheballs;

import javafx.scene.layout.Pane;
import nl.marcmanning.avoidtheballs.systems.CollisionDetector;
import nl.marcmanning.avoidtheballs.systems.MovementSystem;
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
        this.renderer = new Renderer(entitySpace);
        this.systems = new HashSet<>();
        init();
    }

    private void init() {
        systems.add(new MovementSystem(entitySpace));
        systems.add(new CollisionDetector(entitySpace));
    }

    public void start() {
        running = true;
        new Thread(this::run).start();
    }

    public void stop() {
        running = false;
    }

    private void run() {
        int loops;
        float interpolation;
        long nextGameTick = getTimeMillis();
        running = true;
        while (running) {
            loops = 0;
            while (getTimeMillis() > nextGameTick && loops < Constants.MAX_FRAME_SKIPS) {
                update();
                nextGameTick += Constants.TICK_DURATION_MILLIS;
                loops++;
            }
            interpolation = ((float) (getTimeMillis() + Constants.TICK_DURATION_MILLIS - nextGameTick)
                    / Constants.TICK_DURATION_MILLIS);
            renderer.display(interpolation);
        }
    }

    private void update() {
        for (System system : systems) {
            system.update();
        }
    }

    private long getTimeMillis() {
        return java.lang.System.nanoTime() / 1000000;
    }
}
