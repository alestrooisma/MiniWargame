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
    private final Skin ellipseTop = new Skin(new Texture(Gdx.files.internal("ellipse-top.png")), 36, 17);
    private final Skin ellipseBottom = new Skin(new Texture(Gdx.files.internal("ellipse-bottom.png")), 36, 17);
    private final Skin targetTop = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-top.png")), 36, 17);
    private final Skin targetBottom = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-bottom.png")), 36, 17);
    private final Skin spear = new Skin(new Texture(Gdx.files.internal("spear.png")), 39.5f, 33.5f);
    // Not owned
    private final BattleController controller;
    private final Camera cam;
    private Army player = null;
    // Utilities
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
        BattleController.Interaction interaction = getInteraction();

        // Prepare drawing
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // Render all elements (with decoration)
        elements.sort();
        for (Element e : elements) {
            if (e.getUnit() != null) {
                if (e.getUnit() == controller.getTarget()) {
                    switch (interaction) {
                        case SELECT:
                            renderUnit(e, ellipseTop, ellipseBottom, 0.75f);
                            break;
                        case CHARGE:
                        case RANGED:
                            renderUnit(e, targetTop, targetBottom, 0.75f);
                            break;
                    }
                } else if (e.getUnit() == controller.getSelected()) {
                    renderUnit(e, ellipseTop, ellipseBottom);
                } else {
                    renderUnit(e, ellipseTop, ellipseBottom, 0.5f);
                }
            } else {
                e.getSkin().draw(batch, e.getPosition(), e.getRotation());
            }
        }

        // Render indicator for movement target position
        switch (interaction) {
            case MOVE:
            case CHARGE:
                worldToPixelCoordinates(controller.getDestination(), pixel);
                batch.setColor(1, 1, 1, 0.5f);
                ellipseTop.draw(batch, pixel);
                ellipseBottom.draw(batch, pixel);
                batch.setColor(1, 1, 1, 1);
                break;
        }

        // Finalize drawing
        batch.end();
    }

    private BattleController.Interaction getInteraction() {
        // Get unit beneath mouse cursor
        cam.unproject(pixel.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        Element hoveredElement = getElementAt(pixel);
        Unit hoveredUnit = hoveredElement != null ? hoveredElement.getUnit() : null;

        // Get interaction from controller (what happens if player presses LMB)
        pixelToWorldCoordinates(pixel, world);
        return controller.determineInteraction(world.x, world.y, hoveredUnit);
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

    public static void worldToPixelCoordinates(Vector2 world, Vector3 pixel) {
        pixel.x = world.x;
        pixel.y = world.y / 2;
        pixel.z = 0;
    }

    public static void pixelToWorldCoordinates(Vector3 pixel, Vector2 world) {
        pixelToWorldCoordinates(pixel.x, pixel.y, world);
    }

    public static void pixelToWorldCoordinates(float x, float y, Vector2 world) {
        world.x = x;
        world.y = y * 2;
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
        Element element = findElement(controller.getSelected());
        if (element != null) {
            worldToPixelCoordinates(event.getDestination(), pixel);
            engine.add(element.getPosition(), pixel, 300);
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
