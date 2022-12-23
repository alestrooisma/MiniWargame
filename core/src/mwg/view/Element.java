package mwg.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import mwg.model.Unit;

public class Element implements Comparable<Element>, Disposable {
    // Owned
    private final Vector2 position = new Vector2();
    // Not owned
    private final Unit unit;
    private final Skin skin;

    public Element(Unit unit, Skin skin) {
        this.unit = unit;
        this.skin = skin;
        position.set(unit.getPosition());
    }

    public Vector2 getPosition() {
        return position;
    }

    public Unit getUnit() {
        return unit;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean contains(float x, float y) {
        return skin.contains(x - position.x, y - position.y);
    }

    public void dispose() {
    }

    @Override
    public int compareTo(Element other) {
        return Float.compare(other.position.y, this.position.y);
    }
}
