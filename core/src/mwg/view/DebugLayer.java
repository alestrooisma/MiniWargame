package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class DebugLayer implements Layer {
    // Owned
    private final ShapeRenderer renderer = new ShapeRenderer();
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
        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public void toggle() {
        enabled = !enabled;
    }
}
