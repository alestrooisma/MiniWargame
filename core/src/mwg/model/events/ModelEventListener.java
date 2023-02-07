package mwg.model.events;

import mwg.model.Battle;
import mwg.model.GameState;

public class ModelEventListener implements EventListener {
    // Not owned
    private final GameState state;

    public ModelEventListener(GameState state) {
        this.state = state;
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
        state.add(event);
        event.getUnit().setPosition(event.getDestination());
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        state.add(event);
    }
}
