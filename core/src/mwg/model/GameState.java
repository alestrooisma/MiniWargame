package mwg.model;

import com.badlogic.gdx.utils.Array;
import mwg.model.events.Event;

public class GameState {
    // Owned
    private final Array<Event> events = new Array<>();
    private int currentPlayerIndex;
    // Not owned
    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
        currentPlayerIndex = -1;
        nextTurn();
    }

    public Army getCurrentPlayer() {
        return battle.getArmies().get(currentPlayerIndex);
    }

    public void add(Event event) {
        events.add(event);
    }

    public void nextTurn() {
        // Set next player as current
        currentPlayerIndex = (currentPlayerIndex + 1) % battle.getArmies().size;

        // Reset event log
        events.clear();
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
