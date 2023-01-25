package mwg.controller;

import com.badlogic.gdx.math.Vector2;
import mwg.controller.events.EventDealer;
import mwg.controller.events.MoveEvent;
import mwg.model.Battle;
import mwg.model.Unit;

public class BattleController {
    // Owned
    private final EventDealer dealer = new EventDealer();
    private final Pathfinder pathfinder = new Pathfinder();
    // Not owned
    private Battle battle;
    private Unit selected = null;
    // Utilities
    private final Vector2 destination = new Vector2();

    public Unit getSelected() {
        return selected;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
        pathfinder.setBattle(battle);
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public EventDealer getDealer() {
        return dealer;
    }

    public void interact(float x, float y, Unit touched) {
        if (selected == null) {
            if (touched.getArmy() == battle.getArmies().first()) {
                selected = touched;
            }
        } else {
            pathfinder.determineMovementDestinationTowards(selected, x, y, destination);
            if (pathfinder.isDestinationAvailable(selected, destination)) {
                dealer.deal(new MoveEvent(selected, destination));
            }
        }
    }

    public void cancel() {
        selected = null;
    }
}
