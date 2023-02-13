package mwg.controller.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import mwg.model.Unit;

public interface AI {
    void update();

    Array<Targeting> getTargeting();

    class Targeting {
        public final Unit unit;
        public final Unit target;
        public final Vector2 destination = new Vector2();
        public Action action = Action.NONE;

        public Targeting(Unit unit, Unit target) {
            this.unit = unit;
            this.target = target;
        }
    }

    enum Action {
        NONE, MOVE, CHARGE, RANGED
    }
}
