package mwg.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Skin implements Disposable {
    // Owned
    private final Texture texture;
    private final Vector2 origin;
    private final Shape2D bounds;

    public Skin(Texture texture, float originX, float originY) {
        this(texture, originX, originY, null);
    }

    public Skin(Texture texture, float originX, float originY, Shape2D bounds) {
        this.texture = texture;
        this.origin = new Vector2(originX, originY);
        this.bounds = bounds;
    }

    public Shape2D getBounds() {
        return bounds;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x - origin.x, y - origin.y);
    }

    public void draw(SpriteBatch batch, Vector2 position) {
        draw(batch, position.x, position.y);
    }

    public void draw(SpriteBatch batch, Vector3 position) {
        draw(batch, position.x, position.y);
    }

    public boolean contains(float x, float y) {
        return bounds != null && bounds.contains(x, y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
