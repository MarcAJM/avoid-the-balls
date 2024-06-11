package nl.marcmanning.avoidtheballs.systems;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import nl.marcmanning.avoidtheballs.extra.Constants;
import nl.marcmanning.avoidtheballs.extra.Entity;
import nl.marcmanning.avoidtheballs.EntitySpace;
import nl.marcmanning.avoidtheballs.Game;
import nl.marcmanning.avoidtheballs.components.Hitbox;
import nl.marcmanning.avoidtheballs.components.Movement;
import nl.marcmanning.avoidtheballs.components.Rendering;
import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

import java.util.Random;
import java.util.Stack;

public class Spawner extends System {
    private long spawnTime;
    private final Stack<Entity> entities;

    public Spawner(EntitySpace entitySpace, long startingTime) {
        super(entitySpace);
        this.spawnTime = startingTime;
        this.entities = new Stack<>();
        init();
    }

    @Override
    public void update() {
        if (spawnTime <= Game.getTimeMillis() && !entities.isEmpty()) {
            spawnTime += Constants.SPAWN_DELAY;
            entitySpace.add(entities.pop());
        }
    }

    private void init() {
        for (int i = 1; i < Constants.SPAWN_LIMIT + 1; i++) {
            Entity entity = generateRandomEntity(i);
            entities.push(entity);
        }
    }

    private Entity generateRandomEntity(int id) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double radius = generateRandomRadius();
        Color color = generateRandomColor();
        RealVector pos = generateRandomPosition(bounds, radius);
        RealVector vel = generateRandomVelocity(bounds, pos, radius);
        return new Entity(id, new Hitbox(radius), new Rendering(pos.getEntry(0),
                pos.getEntry(1), radius, color), new Movement(pos, vel));
    }

    private RealVector generateRandomVelocity(Rectangle2D bounds, RealVector pos, double radius) {
        double speed = generateRandomSpeed();
        RealVector direction = generateRandomDirection(bounds, pos, radius);
        return direction.mapMultiply(speed);
    }

    private RealVector generateRandomDirection(Rectangle2D bounds, RealVector pos, double radius) {
        Random random = new Random();
        double x = radius + random.nextDouble(bounds.getWidth() - 2 * radius);
        double y = radius + random.nextDouble(bounds.getHeight() - 2 * radius);
        RealVector endPos = new ArrayRealVector(new double[]{x, y});
        return endPos.subtract(pos).unitVector();
    }

    private RealVector generateRandomPosition(Rectangle2D bounds, double radius) {
        Random random = new Random();
        int index = random.nextInt(4);
        return switch (index) {
            case 0 -> new ArrayRealVector(new double[]{random.nextDouble(bounds.getWidth()), -radius});
            case 1 -> new ArrayRealVector(new double[]{bounds.getWidth() + radius, random.nextDouble(bounds.getHeight())});
            case 2 -> new ArrayRealVector(new double[]{random.nextDouble(bounds.getWidth()), bounds.getHeight() + radius});
            default -> new ArrayRealVector(new double[]{-radius, random.nextDouble(bounds.getHeight())});
        };
    }

    private Color generateRandomColor() {
        Random random = new Random();
        int index = random.nextInt(Constants.COLORS.length);
        return Constants.COLORS[index];
    }

    private double generateRandomSpeed() {
        return generateRandomInterval(Constants.MAX_SPEED, Constants.MIN_SPEED);
    }

    private double generateRandomRadius() {
        return generateRandomInterval(Constants.MAX_RADIUS, Constants.MIN_RADIUS);
    }

    private double generateRandomInterval(double x, double y) {
        Random random = new Random();
        return random.nextDouble(x - y) + y;
    }
}
