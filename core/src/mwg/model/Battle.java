package mwg.model;

import com.badlogic.gdx.utils.Array;
import mwg.controller.events.Event;

public class Battle {
    // Owned
    private final Array<Army> armies = new Array<>();
    private final Array<Event> events = new Array<>();

    public void add(Army army) {
        armies.add(army);
    }

    public void add(Event event) {
        events.add(event);
    }

    public Array<Army> getArmies() {
        return armies;
    }

    public boolean mayMove(Unit unit) {
        for (Event event : events) {
            if (event.getUnit() == unit) {
                return false;
            }
        }
        return true;
    }

    public boolean mayPerformRangedAttack(Unit unit) {
        for (Event event : events) {
            if (event.getUnit() == unit) {
                return false;
            }
        }
        return true;
    }
}
