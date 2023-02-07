package mwg.model.events;

import com.badlogic.gdx.math.Vector2;
import mwg.model.Unit;

public class MoveEvent extends Event {
    private final Vector2 destination = new Vector2();

    public MoveEvent(Unit unit, Vector2 destination) {
        super(unit);
        this.destination.set(destination);
    }

    public Vector2 getDestination() {
        return destination;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleMoveEvent(this);
    }
}
