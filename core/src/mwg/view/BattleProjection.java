package mwg.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BattleProjection extends Projection {

    public BattleProjection(Viewport viewport) {
        super(viewport);
    }

    public void worldToPixelCoordinates(Vector2 world, Vector3 pixel) {
        worldToPixelCoordinates(world.x, world.y, pixel);
    }

    public void worldToPixelCoordinates(float x, float y, Vector3 pixel) {
        pixel.x = x * 25;
        pixel.y = y / 2 * 25;
        pixel.z = 0;
    }

    public void pixelToWorldCoordinates(Vector3 pixel, Vector2 world) {
        pixelToWorldCoordinates(pixel.x, pixel.y, world);
    }

    public void pixelToWorldCoordinates(float x, float y, Vector2 world) {
        world.x = x / 25f;
        world.y = y * 2 / 25f;
    }
}
