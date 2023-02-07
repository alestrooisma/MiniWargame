package mwg.model.events;

import mwg.model.Battle;

public class ModelEventListener implements EventListener {
    // Not owned
    private Battle battle;

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
        battle.add(event);
        event.getUnit().setPosition(event.getDestination());
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        battle.add(event);
    }
}
