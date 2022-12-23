package mwg.model;

import com.badlogic.gdx.utils.Array;

public class Army {
    // Owned
    private final Array<Unit> units;

    public Army(int size) {
        this.units = new Array<>(size);
    }

    public void add(Unit unit) {
        units.add(unit);
    }

    public Array<Unit> getUnits() {
        return units;
    }
}
