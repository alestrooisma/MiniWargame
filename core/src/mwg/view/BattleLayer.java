package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BattleLayer implements Layer {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final Array<Element> elements = new Array<>();
    private final TweenEngine engine = new TweenEngine();
    private final Skin hoverTop = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-top.png")), 36, 18);
    private final Skin hoverBottom = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-bottom.png")), 36, 18);
    private final Skin selectionTop = new Skin(new Texture(Gdx.files.internal("ellipse-top.png")), 36, 18);
    private final Skin selectionBottom = new Skin(new Texture(Gdx.files.internal("ellipse-bottom.png")), 36, 18);
    // Not owned
    private final Camera cam;
    private Element selected = null;
    // Utilities
    private final Vector3 vec = new Vector3();

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
        engine.update(dt);
    }

    @Override
    public void render() {
        // Get element beneath mouse cursor
        cam.unproject(vec.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Element hovered = getElementAt(vec.x, vec.y);

        // Render all elements + hover & selection decoration
        elements.sort();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for (Element e : elements) {
            if (e == selected) {
                selectionTop.draw(batch, e.getPosition());
                e.getSkin().draw(batch, e.getPosition());
                selectionBottom.draw(batch, e.getPosition());
            } else if (e == hovered) {
                hoverTop.draw(batch, e.getPosition());
                e.getSkin().draw(batch, e.getPosition());
                hoverBottom.draw(batch, e.getPosition());
            } else {
                e.getSkin().draw(batch, e.getPosition());
            }
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

    public void touch(int button, float x, float y) {
        if (button == Buttons.LEFT) {
            selected = getElementAt(x, y);
        } else if (selected != null && button == Buttons.RIGHT) {
            engine.add(selected.getPosition(), x, y, 300);
        }
    }

    private Element getElementAt(float x, float y) {
        Element touchedElement = null;

        // Iterate the elements in reverse to get the topmost element
        // for which the coordinates are in its "hit box".
        for (int i = elements.size - 1; touchedElement == null && i >= 0; i--) {
            Element e = elements.get(i);
            if (e.contains(x, y)) {
                touchedElement = e;
            }
        }

        // Return the topmost element at (x, y), which may be null
        return touchedElement;
    }
}
