package mwg.model;

import com.badlogic.gdx.utils.Array;

public class Battle {
    // Owned
    private final Array<Army> armies = new Array<>();

    public void add(Army army) {
        armies.add(army);
    }

    public Array<Army> getArmies() {
        return armies;
    }
}
