package mwg.controller.events;

import mwg.model.Unit;

public abstract class Event {

    private final Unit unit;

    public Event(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    abstract void accept(EventListener listener);
}
