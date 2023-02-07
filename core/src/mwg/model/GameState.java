package mwg.model;

import com.badlogic.gdx.utils.Array;
import mwg.model.events.Event;

public class GameState {
    // Owned
    private final Array<Event> events = new Array<>();
    // Not owned
    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void add(Event event) {
        events.add(event);
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
