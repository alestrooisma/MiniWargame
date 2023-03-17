package mwg.view;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import mwg.model.Unit;

public class Element implements Comparable<Element>, Disposable {
    // Owned
    private final Vector3 position = new Vector3();
    private float rotation = 0;
    // Not owned
    private final Unit unit;
    private final Skin skin;

    public Element(Unit unit) {
        this(unit, ResourceContainer.instance.get(unit.getType().getName()));
    }

    public Element(Unit unit, Skin skin) {
        this.unit = unit;
        this.skin = skin;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Unit getUnit() {
        return unit;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean contains(float x, float y) {
        return skin.getBounds() != null && skin.contains(x - position.x, y - position.y);
    }

    public void dispose() {
    }

    @Override
    public int compareTo(Element other) {
        return Float.compare(other.position.y, this.position.y);
    }
}
