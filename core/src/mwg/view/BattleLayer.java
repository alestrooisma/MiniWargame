package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import mwg.controller.BattleController;
import mwg.model.Army;

public class BattleLayer implements Layer {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final Array<Element> elements = new Array<>();
    private final TweenEngine engine = new TweenEngine();
    private final Skin hoverTop = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-top.png")), 36, 17);
    private final Skin hoverBottom = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-bottom.png")), 36, 17);
    private final Skin selectionTop = new Skin(new Texture(Gdx.files.internal("ellipse-top.png")), 36, 17);
    private final Skin selectionBottom = new Skin(new Texture(Gdx.files.internal("ellipse-bottom.png")), 36, 17);
    // Not owned
    private final BattleController controller;
    private final Camera cam;
    private Army player = null;
    private Element selected = null;
    // Utilities
    private final Vector3 mousePixelPosition = new Vector3();
    private final Vector3 movementPixelDestination = new Vector3();
    private final Vector2 movementWorldDestination = new Vector2();
    private final Vector2 world = new Vector2();

    public BattleLayer(BattleController controller, Camera cam) {
        this.controller = controller;
        this.cam = cam;
    }

    public void add(Element e) {
        elements.add(e);
        // Set the element position to the pixel coordinates matching the unit's world coordinates
        worldToPixelCoordinates(e.getUnit().getPosition(), e.getPosition());
    }

    public void setPlayerArmy(Army player) {
        this.player = player;
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
        cam.unproject(mousePixelPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Element hovered = getElementAt(mousePixelPosition);

        // Prepare drawing
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // Render all elements (with decoration)
        elements.sort();
        for (Element e : elements) {
            if (e == selected) {
                renderElement(e, selectionTop, selectionBottom);
            } else if (e == hovered && e.getUnit().getArmy() == player) {
                renderElement(e, selectionTop, selectionBottom, 0.75f);
            } else if (e == hovered) { // Opponent army
                renderElement(e, hoverTop, hoverBottom, 0.75f);
            } else {
                renderElement(e, selectionTop, selectionBottom, 0.5f);
            }
        }

        // Render indicator for movement target position
        if (selected != null) {
            determineMovementDestination(mousePixelPosition);
            batch.setColor(1, 1, 1, 0.5f);
            if (canBeMovedTo(movementWorldDestination)) {
                selectionTop.draw(batch, movementPixelDestination);
                selectionBottom.draw(batch, movementPixelDestination);
            } else {
                hoverTop.draw(batch, movementPixelDestination);
                hoverBottom.draw(batch, movementPixelDestination);
            }
            batch.setColor(1, 1, 1, 1);
        }

        batch.end();
    }

    public void renderElement(Element e, Skin top, Skin bottom) {
        renderElement(e, top, bottom, 1);
    }

    public void renderElement(Element e, Skin top, Skin bottom, float alpha) {
        Color c = e.getUnit().getArmy() == player ? Color.BLUE : Color.RED;

        // Draw top of selection ring
        batch.setColor(c.r, c.g, c.b, alpha);
        top.draw(batch, e.getPosition());

        // Draw element itself
        batch.setColor(Color.WHITE);
        e.getSkin().draw(batch, e.getPosition());

        // Draw bottom of selection ring
        batch.setColor(c.r, c.g, c.b, alpha);
        bottom.draw(batch, e.getPosition());

        // Reset color
        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Element e : elements) {
            e.dispose();
        }
    }

    public void touch(int button, float x, float y) {
        if (engine.isBusy()) {
            return;
        }

        Element touched = getElementAt(x, y);
        if (button == Buttons.LEFT && (touched == null || touched.getUnit().getArmy() == player)) {
            selected = touched;
        } else if (engine.isIdle() && selected != null && button == Buttons.RIGHT) {
            determineMovementDestination(x, y);
            if (canBeMovedTo(movementWorldDestination)) {
                selected.getUnit().setPosition(movementWorldDestination.x, movementWorldDestination.y);
                engine.add(selected.getPosition(), movementPixelDestination, 300);
            }
        }
    }

    private Element getElementAt(Vector3 position) {
        return getElementAt(position.x, position.y);
    }

    private Element getElementAt(float x, float y) {
        Element touchedElement = null;
        pixelToWorldCoordinates(x, y, world);

        // Iterate the elements in reverse to get the topmost element
        // for which the coordinates are in its "hit box".
        for (int i = elements.size - 1; touchedElement == null && i >= 0; i--) {
            Element e = elements.get(i);
            if (e.contains(x, y) || e.getUnit().occupies(world.x, world.y)) {
                touchedElement = e;
            }
        }

        // Return the topmost element at (x, y), which may be null
        return touchedElement;
    }

    private boolean canBeMovedTo(Vector2 position) {
        return canBeMovedTo(position.x, position.y);
    }

    private boolean canBeMovedTo(float x, float y) {
        for (Element e : elements) {
            if (e != selected && e.getUnit().overlaps(x, y, selected.getUnit().getRadius())) {
                return false;
            }
        }
        return true;
    }

    private void determineMovementDestination(Vector3 position) {
        determineMovementDestination(position.x, position.y);
    }

    private void determineMovementDestination(float x, float y) {
        pixelToWorldCoordinates(x, y, world);
        controller.getPathfinder().determineMovementDestinationTowards(selected.getUnit(), world.x, world.y, movementWorldDestination);
        worldToPixelCoordinates(movementWorldDestination, movementPixelDestination);
    }

    private static Vector3 worldToPixelCoordinates(Vector2 world, Vector3 pixel) {
        pixel.x = world.x;
        pixel.y = world.y / 2;
        pixel.z = 0;
        return pixel;
    }

    private static Vector2 pixelToWorldCoordinates(float x, float y, Vector2 world) {
        world.x = x;
        world.y = y * 2;
        return world;
    }

    /**
     * Read-only! Intended for debug purposes.
     * @return the list of elements rendered by this layer
     */
    Array<Element> getElements() {
        return elements;
    }
}
