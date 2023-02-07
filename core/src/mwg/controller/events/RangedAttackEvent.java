package mwg.controller.events;

import mwg.model.Unit;

public class RangedAttackEvent extends Event {
    private final Unit target;

    public RangedAttackEvent(Unit attacker, Unit target) {
        super(attacker);
        this.target = target;
    }

    public Unit getTarget() {
        return target;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleRangedAttackEvent(this);
    }
}
