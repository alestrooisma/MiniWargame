package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import mwg.model.Army;

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
    private Army player = null;
    private Element selected = null;
    // Utilities
    private final Vector3 mousePosition = new Vector3();

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
        cam.unproject(mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Element hovered = getElementAt(mousePosition);

        // Prepare drawing
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // Render all elements + hover & selection decoration
        elements.sort();
        for (Element e : elements) {
            if (e == selected) {
                selectionTop.draw(batch, e.getPosition());
                e.getSkin().draw(batch, e.getPosition());
                selectionBottom.draw(batch, e.getPosition());
            } else if (e == hovered && e.getUnit().getArmy() == player) {
                hoverTop.draw(batch, e.getPosition());
                e.getSkin().draw(batch, e.getPosition());
                hoverBottom.draw(batch, e.getPosition());
            } else {
                e.getSkin().draw(batch, e.getPosition());
            }
        }

        // Render indicator for movement target position
        if (selected != null) {
            batch.setColor(1, 1, 1, 0.5f);
            selectionTop.draw(batch, mousePosition);
            selectionBottom.draw(batch, mousePosition);
            batch.setColor(1, 1, 1, 1);
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
        Element touched = getElementAt(x, y);
        if (button == Buttons.LEFT && (touched == null || touched.getUnit().getArmy() == player)) {
            selected = touched;
        } else if (selected != null && button == Buttons.RIGHT) {
            selected.getUnit().setPosition(x, y);
            engine.add(selected.getPosition(), selected.getUnit().getPosition(), 300);
        }
    }

    private Element getElementAt(Vector3 position) {
        return getElementAt(position.x, position.y);
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

    public void setPlayerArmy(Army player) {
        this.player = player;
    }
}
