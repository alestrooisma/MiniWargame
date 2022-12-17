package mwg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import aetherdriven.view.Layer;

public class BattleLayer implements Layer {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final Array<Element> elements = new Array<>();

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
}
