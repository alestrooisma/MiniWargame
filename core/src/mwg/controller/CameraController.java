package mwg.controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import mwg.model.GameState;
import mwg.view.Projection;

public class CameraController {
    private final GameState state;
    private final Projection projection;
    private final Vector3 pixel = new Vector3();
    private final Vector2 world = new Vector2();

    public CameraController(GameState state, Projection projection) {
        this.state = state;
        this.projection = projection;
    }

    public void center() {
        Rectangle bounds = state.getBattle().getBounds();
        set(bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);
    }

    public void set(float x, float y) {
        // Get viewport with in world coordinates
        projection.pixelToWorldCoordinates(projection.getViewport().getWorldWidth() / 2, projection.getViewport().getWorldHeight() / 2, world);
        float halfWorldWidth = world.x;
        float halfWorldHeight = world.y;

        // Determine minimum and maximum allowed position for the camera
        Rectangle bounds = state.getBattle().getBounds();
        float minX = bounds.x + halfWorldWidth;
        float minY = bounds.y + halfWorldHeight;
        float maxX = bounds.x + bounds.width - halfWorldWidth;
        float maxY = bounds.y + bounds.height - halfWorldHeight;

        // Limit camera position
        x = MathUtils.clamp(x, minX, maxX);
        y = MathUtils.clamp(y, minY, maxY);

        // Actually set the camera position
        projection.worldToPixelCoordinates(x, y, pixel);
        projection.getCamera().position.set(pixel.x, pixel.y, pixel.z);
    }

    public void move(float dx, float dy) {
        projection.pixelToWorldCoordinates(projection.getCamera().position, world);
        set(world.x + dx, world.y + dy);
    }
}
