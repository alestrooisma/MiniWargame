package mwg.controller;

import mwg.model.Battle;
import mwg.model.Unit;

public class BattleController {
    // Owned
    private final Pathfinder pathfinder = new Pathfinder();
    // Not owned
    private Battle battle;
    private Unit selected = null;

    public Unit getSelected() {
        return selected;
    }

    public void setSelected(Unit selected) {
        this.selected = selected;
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
}
