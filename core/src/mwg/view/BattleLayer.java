package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import mwg.controller.BattleController;
import mwg.controller.CameraController;
import mwg.model.Army;
import mwg.model.Unit;
import mwg.model.events.EndTurnEvent;
import mwg.model.events.EventListener;
import mwg.model.events.MoveEvent;
import mwg.model.events.RangedAttackEvent;
import mwg.model.events.StartTurnEvent;

public class BattleLayer implements Layer, EventListener {
    // Owned
    private final SpriteBatch batch = new SpriteBatch();
    private final TextRenderer textRenderer = new TextRenderer(batch);
    private final Array<Element> elements = new Array<>();
    private final TweenEngine engine = new TweenEngine();
    private final CameraController cameraController;
    private final Skin ellipseTop = new Skin(new Texture(Gdx.files.internal("ellipse-top.png")), 36, 17);
    private final Skin ellipseBottom = new Skin(new Texture(Gdx.files.internal("ellipse-bottom.png")), 36, 17);
    private final Skin targetTop = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-top.png")), 36, 17);
    private final Skin targetBottom = new Skin(new Texture(Gdx.files.internal("ellipse-nozoc-bottom.png")), 36, 17);
    private final Skin spear = new Skin(new Texture(Gdx.files.internal("spear.png")), 39.5f, 33.5f);
    // Not owned
    private final BattleController controller;
    private final Projection projection;
    private Army player = null;
    // Utilities
    private final Vector2 world = new Vector2();
    private final Vector3 pixel = new Vector3();
    private final Vector3 origin = new Vector3();
    private final Vector3 target = new Vector3();

    public BattleLayer(BattleController controller, Projection projection) {
        this.controller = controller;
        this.projection = projection;
        this.cameraController = controller.createCameraController(projection);
        this.cameraController.center();
    }

    public void add(Element e) {
        elements.add(e);
        // Set the element position to the pixel coordinates matching the unit's world coordinates
        if (e.getUnit() != null) {
            projection.worldToPixelCoordinates(e.getUnit().getPosition(), e.getPosition());
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
        // Ugly temporary camera controls
        final float v = 10;
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            cameraController.move(-v*dt, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            cameraController.move(v*dt, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            cameraController.move(0, -v*dt);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            cameraController.move(0, v*dt);
        }

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
        batch.setProjectionMatrix(projection.getCamera().combined);
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
                projection.worldToPixelCoordinates(controller.getDestination(), pixel);
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
        projection.screenToPixelCoordinates(Gdx.input.getX(), Gdx.input.getY(), pixel);
        Element hoveredElement = getElementAt(pixel);
        Unit hoveredUnit = hoveredElement != null ? hoveredElement.getUnit() : null;

        // Get interaction from controller (what happens if player presses LMB)
        projection.pixelToWorldCoordinates(pixel, world);
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

        // Draw unit name
        if (Gdx.input.isKeyPressed(Keys.ALT_LEFT)) {
            textRenderer.draw(e.getUnit().getName(), e.getPosition().x, e.getPosition().y - 5, Align.top);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Element e : elements) {
            e.dispose();
        }
    }

    public void touch(int x, int y, int button) {
        if (button == Buttons.LEFT && engine.isIdle()) {
            // Do some coordinate conversions
            projection.screenToPixelCoordinates(x, y, pixel);
            projection.pixelToWorldCoordinates(x, y, world);
            // Find the touched element and let the controller deal with this input
            Element touched = getElementAt(pixel.x, pixel.y);
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
        projection.pixelToWorldCoordinates(x, y, world);

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

    @Override
    public void handleStartTurnEvent(StartTurnEvent event) {
        System.out.println("Start turn " + event.getRound() + " for player " + (event.getPlayerIndex() + 1));
    }

    @Override
    public void handleEndTurnEvent(EndTurnEvent event) {
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
        Element element = findElement(event.getUnit());
        if (element != null) {
            projection.worldToPixelCoordinates(event.getDestination(), pixel);
            engine.add(element.getPosition(), pixel, 300);
        }
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        projection.worldToPixelCoordinates(event.getUnit().getPosition(), origin);
        projection.worldToPixelCoordinates(event.getTarget().getPosition(), target);
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
