package mwg.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Battle {
    // Owned
    private final Rectangle bounds;
    private final Array<Army> armies = new Array<>();

    public Battle(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void add(Army army) {
        armies.add(army);
    }

    public Array<Army> getArmies() {
        return armies;
    }
}
