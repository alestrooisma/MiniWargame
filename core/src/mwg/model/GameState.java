package mwg.model;

import com.badlogic.gdx.utils.Array;
import mwg.model.events.Event;

public class GameState {
    // Owned
    private final Array<Event> events = new Array<>();
    private int currentPlayerIndex;
    private int round;
    // Not owned
    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Army getCurrentPlayer() {
        return battle.getArmies().get(currentPlayerIndex);
    }

    public int getRound() {
        return round;
    }

    public void add(Event event) {
        events.add(event);
    }

    public void startTurn(int playerIndex, int round) {
        this.currentPlayerIndex = playerIndex;
        this.round = round;
    }

    public void endTurn() {
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
