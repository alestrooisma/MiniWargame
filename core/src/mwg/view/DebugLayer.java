package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import mwg.controller.CameraController;
import mwg.model.events.EndTurnEvent;
import mwg.model.events.EventListener;
import mwg.model.events.MoveEvent;
import mwg.model.events.RangedAttackEvent;
import mwg.model.events.StartTurnEvent;

public class DebugLayer implements Layer, EventListener {
    // Owned
    private final ShapeRenderer renderer = new ShapeRenderer();
    private final Vector3 origin = new Vector3();
    private final Vector3 target = new Vector3();
    private final Vector3 ellipse = new Vector3();
    private final Rectangle bounds = new Rectangle();
    // Not owned
    private final CameraController cameraController;
    private final BattleLayer battleLayer;
    private final Projection projection;
    private boolean enabled = false;

    public DebugLayer(BattleLayer battleLayer, Projection projection, CameraController cameraController) {
        this.battleLayer = battleLayer;
        this.projection = projection;
        this.cameraController = cameraController;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        if (!enabled) {
            return;
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setProjectionMatrix(projection.getCamera().combined);

        renderer.setColor(Color.MAGENTA);
        for (Element e : battleLayer.getElements()) {
            if (e.getSkin().getBounds() != null) {
                Rectangle rect = (Rectangle) e.getSkin().getBounds();
                renderer.rect(e.getPosition().x + rect.x, e.getPosition().y + rect.y, rect.width, rect.height);
            }
            if (e.getUnit() != null) {
                float r = e.getUnit().getRadius();
                projection.worldToPixelCoordinates(r, r, ellipse);
                renderEllipse(renderer, e.getPosition(), ellipse);
            }
        }
        renderer.line(origin, target);

        renderer.setColor(Color.RED);
        projection.worldToPixelCoordinates(cameraController.getBounds(), bounds);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        renderer.end();
    }

    private static void renderEllipse(ShapeRenderer renderer, Vector3 center, Vector3 axes) {
        renderer.ellipse(center.x - axes.x, center.y - axes.y, axes.x * 2, axes.y * 2);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public void toggle() {
        enabled = !enabled;
    }

    @Override
    public void handleStartTurnEvent(StartTurnEvent event) {
    }

    @Override
    public void handleEndTurnEvent(EndTurnEvent event) {
    }

    @Override
    public void handleMoveEvent(MoveEvent event) {
    }

    @Override
    public void handleRangedAttackEvent(RangedAttackEvent event) {
        projection.worldToPixelCoordinates(event.getUnit().getPosition(), origin);
        projection.worldToPixelCoordinates(event.getTarget().getPosition(), target);
    }
}
