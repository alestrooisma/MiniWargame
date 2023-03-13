package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import mwg.controller.ai.AI;

public class AiLayer implements Layer {
    // Owned
    private final ShapeRenderer renderer = new ShapeRenderer();
    private final Vector3 origin = new Vector3();
    private final Vector3 target = new Vector3();
    private final Vector3 destination = new Vector3();
    private final Vector3 ellipse = new Vector3();
    private final Vector3 limits = new Vector3();
    // Not owned
    private final Projection projection;
    private AI ai = null;

    public AiLayer(Projection projection) {
        this.projection = projection;
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void update(float dt) {
        if (ai == null) {
            return;
        }

        ai.update();
    }

    @Override
    public void render() {
        if (ai == null) {
            return;
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setProjectionMatrix(projection.getCamera().combined);
        for (AI.Targeting targeting : ai.getTargeting()) {
            projection.worldToPixelCoordinates(targeting.unit.getPosition(), origin);
            projection.worldToPixelCoordinates(targeting.target.getPosition(), target);
            projection.worldToPixelCoordinates(targeting.destination, destination);
            float radius = targeting.unit.getRadius();
            projection.worldToPixelCoordinates(radius, radius, ellipse);
            float range = targeting.unit.getMaxMovement();
            projection.worldToPixelCoordinates(range, range, limits);

            // Draw targeting indicator
            renderer.setColor(Color.ORANGE);
            renderer.line(origin, target);

            // Draw movement range indicator
            renderer.setColor(Color.GRAY);
            renderEllipse(renderer, origin, limits);

            // Draw action
            switch (targeting.action) {
                case MOVE:
                case CHARGE:
                    renderer.setColor(Color.WHITE);
                    renderer.line(origin, destination);
                    renderEllipse(renderer, destination, ellipse);
                    break;
                case RANGED:
                    break;
            }
        }
        renderer.end();
    }

    private static void renderEllipse(ShapeRenderer renderer, Vector3 center, Vector3 axes) {
        renderer.ellipse(center.x - axes.x, center.y - axes.y, axes.x * 2, axes.y * 2);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return DEFAULT_INPUT_PROCESSOR;
    }
}
