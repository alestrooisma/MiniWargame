package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import mwg.controller.BattleController;
import mwg.controller.events.EventListener;
import mwg.controller.events.MoveEvent;
import mwg.controller.events.RangedAttackEvent;
import mwg.model.Army;
import mwg.model.Unit;

public class BattleLayer implements Layer, EventListener {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final Array<Element> elements = new Array<>();
    private final TweenEngine engine = new TweenEngine();
    private final Skin hoverTop = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-top.png")), 36, 17);
    private final Skin hoverBottom = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-bottom.png")), 36, 17);
    private final Skin selectionTop = new Skin(new Texture(Gdx.files.internal("ellipse-top.png")), 36, 17);
    private final Skin selectionBottom = new Skin(new Texture(Gdx.files.internal("ellipse-bottom.png")), 36, 17);
    private final Skin spear = new Skin(new Texture(Gdx.files.internal("spear.png")), 39.5f, 33.5f);
    // Not owned
    private final BattleController controller;
    private final Camera cam;
    private Army player = null;
    // Utilities
    private final Vector3 mousePixelPosition = new Vector3();
    private final Vector3 movementPixelDestination = new Vector3();
    private final Vector2 movementWorldDestination = new Vector2();
    private final Vector2 world = new Vector2();
    private final Vector3 pixel = new Vector3();
    private final Vector3 origin = new Vector3();
    private final Vector3 target = new Vector3();

    public BattleLayer(BattleController controller, Camera cam) {
        this.controller = controller;
        this.cam = cam;
    }

    public void add(Element e) {
        elements.add(e);
        // Set the element position to the pixel coordinates matching the unit's world coordinates
        if (e.getUnit() != null) {
            worldToPixelCoordinates(e.getUnit().getPosition(), e.getPosition());
        }
    }

    public void setPlayerArmy(Army player) {
        this.player = player;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void update(float dt) {
        // Ugly hack to remove animation-only elements when they are not needed anymore
        if (engine.isIdle()) {
            for (Element e : elements) {
                if (e.getUnit() == null) {
                    elements.removeValue(e, true);
                }
            }
        }

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
            if (e.getUnit() != null) {
                if (e.getUnit() == controller.getSelected()) {
                    renderUnit(e, selectionTop, selectionBottom);
                } else if (e == hovered && e.getUnit().getArmy() == player) {
                    renderUnit(e, selectionTop, selectionBottom, 0.75f);
                } else if (e == hovered) { // Opponent army
                    renderUnit(e, hoverTop, hoverBottom, 0.75f);
                } else {
                    renderUnit(e, selectionTop, selectionBottom, 0.5f);
                }
            } else {
                e.getSkin().draw(batch, e.getPosition(), e.getRotation());
            }
        }

        // Render indicator for movement target position
        if (controller.getSelected() != null) {
            determineMovementDestination(mousePixelPosition);
            batch.setColor(1, 1, 1, 0.5f);
            if (isDestinationAvailable()) {
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

    public void renderUnit(Element e, Skin top, Skin bottom) {
        renderUnit(e, top, bottom, 1);
    }

    public void renderUnit(Element e, Skin top, Skin bottom, float alpha) {
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
        if (button == Buttons.LEFT && engine.isIdle()) {
            Element touched = getElementAt(x, y);
            pixelToWorldCoordinates(x, y, world);
            controller.interact(world.x, world.y, touched != null ? touched.getUnit() : null);
        } else if (button == Buttons.RIGHT) {
            controller.cancel();
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
            if (e.contains(x, y) || (e.getUnit() != null && e.getUnit().occupies(world.x, world.y))) {
                touchedElement = e;
            }
        }

        // Return the topmost element at (x, y), which may be null
        return touchedElement;
    }

    private Element findElement(Unit unit) {
        for (Element e : elements) {
            if (e.getUnit() == unit) {
                return e;
            }
        }
        return null;
    }

    private boolean isDestinationAvailable() {
        return controller.getPathfinder().isDestinationAvailable(controller.getSelected(), movementWorldDestination);
    }

    private void determineMovementDestination(Vector3 position) {
        determineMovementDestination(position.x, position.y);
    }

    private void determineMovementDestination(float x, float y) {
        pixelToWorldCoordinates(x, y, world);
        controller.getPathfinder().determineMovementDestinationTowards(controller.getSelected(), world.x, world.y, movementWorldDestination);
        worldToPixelCoordinates(movementWorldDestination, movementPixelDestination);
    }

    public static Vector3 worldToPixelCoordinates(Vector2 world, Vector3 pixel) {
        pixel.x = world.x;
        pixel.y = world.y / 2;
        pixel.z = 0;
        return pixel;
    }

    public static Vector2 pixelToWorldCoordinates(float x, float y, Vector2 world) {
        world.x = x;
        world.y = y * 2;
        return world;
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
        Element element = findElement(controller.getSelected());
        if (element != null) {
            worldToPixelCoordinates(event.getDestination(), movementPixelDestination);
            engine.add(element.getPosition(), movementPixelDestination, 300);
        }
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        worldToPixelCoordinates(event.getAttacker().getPosition(), origin);
        worldToPixelCoordinates(event.getTarget().getPosition(), target);
        origin.z = 20; // TODO determine properly
        target.z = origin.z;

        // Create the projectile element
        Element projectile = new Element(null, spear);
        projectile.setPosition(origin);

        // Set the rotation of the projectile
        pixel.set(target).sub(origin);
        float angle = MathUtils.acos(pixel.dot(0, 1, 0) / pixel.len());
        projectile.setRotation(Math.signum(pixel.x) * angle);
        // angle is the absolute angle between vectors, so it needs to be multiplied by the sign of
        // the x component to get the rotation in the correct direction

        // Add to render list and animation engine
        elements.add(projectile);
        engine.add(projectile.getPosition(), target, 600);
    }

    /**
     * Read-only! Intended for debug purposes.
     * @return the list of elements rendered by this layer
     */
    Array<Element> getElements() {
        return elements;
    }
}
