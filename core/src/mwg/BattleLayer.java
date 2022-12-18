package mwg;

import aetherdriven.view.Layer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class BattleLayer implements Layer {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final Array<Element> elements = new Array<>();
    // Not owned
    private final Camera cam;

    public BattleLayer(Camera cam) {
        this.cam = cam;
    }

    public void add(Element e) {
        elements.add(e);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        elements.sort();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for (Element e : elements) {
            batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Element e : elements) {
            e.dispose();
        }
    }

    public void touch(float x, float y) {
        System.out.printf("Clicked (%.1f, %.1f)%n", x, y);

        // Iterate the elements in reverse to get the topmost element
        // for which the coordinates are in its "hit box".
        for (int i = elements.size - 1; i >= 0; i--) {
            Element e = elements.get(i);
            if (x > e.getPosition().x && x < e.getPosition().x + e.getTexture().getWidth()
                    && y > e.getPosition().y && y < e.getPosition().y + e.getTexture().getHeight()) {
                System.out.println("Touched #" + i + "!");
                break;
            }
        }
    }
}
