package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
    // Not owned
    private final BattleLayer battleLayer;
    private final Camera cam;
    private boolean enabled = false;

    public DebugLayer(BattleLayer battleLayer, Camera cam) {
        this.battleLayer = battleLayer;
        this.cam = cam;
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
        renderer.setProjectionMatrix(cam.combined);
        renderer.setColor(Color.MAGENTA);
        for (Element e : battleLayer.getElements()) {
            if (e.getSkin().getBounds() != null) {
                Rectangle rect = (Rectangle) e.getSkin().getBounds();
                renderer.rect(e.getPosition().x + rect.x, e.getPosition().y + rect.y, rect.width, rect.height);
            }
            if (e.getUnit() != null) {
                float r = e.getUnit().getRadius();
                renderer.ellipse(e.getPosition().x - r, e.getPosition().y - r / 2, r * 2, r);
            }
        }
        renderer.line(origin, target);
        renderer.end();
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
        BattleLayer.worldToPixelCoordinates(event.getUnit().getPosition(), origin);
        BattleLayer.worldToPixelCoordinates(event.getTarget().getPosition(), target);
    }
}
