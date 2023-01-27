package mwg.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

    public void draw(SpriteBatch batch, float x, float y, float rotation) {
        batch.draw(texture, x - origin.x, y - origin.y,
                origin.x, origin.y,
                texture.getWidth(), texture.getHeight(),
                1, 1,
                -rotation * MathUtils.radiansToDegrees,
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
    }

    public void draw(SpriteBatch batch, Vector3 position, float rotation) {
        draw(batch, position.x, position.y, rotation);
    }

    public void draw(SpriteBatch batch, float x, float y) {
        draw(batch, x, y, 0);
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
