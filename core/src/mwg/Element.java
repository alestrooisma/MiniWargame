package mwg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Element implements Disposable {
    // Owned
    private final Vector2 position = new Vector2();
    // Not owned
    private final Texture texture;

    public Element(Texture texture) {
        this.texture = texture;
    }

    public Element(Texture texture, float x, float y) {
        this.texture = texture;
        this.position.x = x;
        this.position.y = y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
    }
}
