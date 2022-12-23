package mwg.model;

import com.badlogic.gdx.math.Vector2;

public class Unit {
    // Owned
    private final Army army;
    private final Vector2 position;

    public Unit(Army army, float x, float y) {
        this(army, new Vector2(x, y));
    }

    public Unit(Army army, Vector2 position) {
        this.army = army;
        this.position = position;
    }

    public Army getArmy() {
        return army;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(float x, float y) {
        position.set(x, y);
    }
}
