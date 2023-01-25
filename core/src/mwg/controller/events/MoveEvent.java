package mwg.controller.events;

import com.badlogic.gdx.math.Vector2;
import mwg.model.Unit;

public class MoveEvent implements Event {
    private final Unit unit;
    private final Vector2 destination = new Vector2();

    public MoveEvent(Unit unit, Vector2 destination) {
        this.unit = unit;
        this.destination.set(destination);
    }

    public Unit getUnit() {
        return unit;
    }

    public Vector2 getDestination() {
        return destination;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleMoveEvent(this);
    }
}
