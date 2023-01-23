package mwg.controller;

import mwg.model.Battle;

public class BattleController {
    // Owned
    private final Pathfinder pathfinder = new Pathfinder();
    // Not owned
    private Battle battle;

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
