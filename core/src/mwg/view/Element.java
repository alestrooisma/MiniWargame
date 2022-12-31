package mwg.view;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import mwg.model.Unit;

public class Element implements Comparable<Element>, Disposable {
    // Owned
    private final Vector3 position = new Vector3();
    // Not owned
    private final Unit unit;
    private final Skin skin;

    public Element(Unit unit, Skin skin) {
        this.unit = unit;
        this.skin = skin;
    }

    public Vector3 getPosition() {
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
