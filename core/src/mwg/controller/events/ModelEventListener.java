package mwg.controller.events;

public class ModelEventListener implements EventListener {
    @Override
    public void handleMoveEvent(MoveEvent event) {
        event.getUnit().setPosition(event.getDestination());
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        System.out.println("Ranged attack!");
    }
}
