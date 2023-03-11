package mwg.view;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Projection {
    protected final Viewport viewport;
    private final Vector3 pixel = new Vector3();

    public Projection(Viewport viewport) {
        this.viewport = viewport;
    }

    public Camera getCamera() {
        return viewport.getCamera();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void update (int screenWidth, int screenHeight) {
        viewport.update(screenWidth, screenHeight);
    }

    public void screenToPixelCoordinates(int x, int y, Vector3 pixel) {
        getCamera().unproject(pixel.set(x, y, 0));
    }

    public void screenToWorldCoordinates(int x, int y, Vector2 world) {
        screenToPixelCoordinates(x, y, pixel);
        pixelToWorldCoordinates(pixel, world);
    }

    public abstract void pixelToWorldCoordinates(Vector3 pixel, Vector2 world);

    public abstract void pixelToWorldCoordinates(float x, float y, Vector2 world);

    public abstract void worldToPixelCoordinates(Vector2 world, Vector3 pixel);

    public abstract void worldToPixelCoordinates(float x, float y, Vector3 pixel);

    public void worldToPixelCoordinates(Rectangle worldRect, Rectangle pixelRect) {
        worldToPixelCoordinates(worldRect.x, worldRect.y, pixel);
        pixelRect.x = pixel.x;
        pixelRect.y = pixel.y;

        worldToPixelCoordinates(worldRect.width, worldRect.height, pixel);
        pixelRect.width = pixel.x;
        pixelRect.height = pixel.y;
    }
}
