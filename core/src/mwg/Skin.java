package mwg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Skin implements Disposable {
    // Owned
    private final Texture texture;
    private final Vector2 origin;

    public Skin(Texture texture, float originX, float originY) {
        this.texture = texture;
        this.origin = new Vector2(originX, originY);
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x - origin.x, y - origin.y);
    }

    public void draw(SpriteBatch batch, Vector2 position) {
        draw(batch, position.x, position.y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
