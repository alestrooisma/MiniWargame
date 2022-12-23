package mwg.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Element implements Comparable<Element>, Disposable {
    // Owned
    private final Vector2 position = new Vector2();
    // Not owned
    private final Skin skin;

    public Element(Skin skin) {
        this.skin = skin;
    }

    public Element(Skin skin, float x, float y) {
        this(skin);
        this.position.x = x;
        this.position.y = y;
    }

    public Vector2 getPosition() {
        return position;
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
