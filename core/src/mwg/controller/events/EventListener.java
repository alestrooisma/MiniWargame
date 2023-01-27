package mwg.controller.events;

public interface EventListener {
    void handleMoveEvent(MoveEvent event);
    void handleRangedAttackEvent(RangedAttackEvent event);
}
