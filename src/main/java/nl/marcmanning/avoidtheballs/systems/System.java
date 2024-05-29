package nl.marcmanning.avoidtheballs.systems;

import nl.marcmanning.avoidtheballs.EntitySpace;

public abstract class System {
    protected final EntitySpace entitySpace;

    public System(EntitySpace entitySpace) {
        this.entitySpace = entitySpace;
    }

    public abstract void update();
}
