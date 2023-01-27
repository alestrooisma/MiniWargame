package mwg.controller.events;

import mwg.model.Unit;

public class RangedAttackEvent implements Event {
    private final Unit attacker, target;

    public RangedAttackEvent(Unit attacker, Unit target) {
        this.attacker = attacker;
        this.target = target;
    }

    public Unit getAttacker() {
        return attacker;
    }

    public Unit getTarget() {
        return target;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleRangedAttackEvent(this);
    }
}
