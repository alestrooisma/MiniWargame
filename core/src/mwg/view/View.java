package mwg.view;

import aetherdriven.view.LayeredView;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class View implements Disposable {
    // Owned
    private final ScreenViewport viewport;
    private final LayeredView view;
    private final BattleLayer battleLayer;

    public View() {
        // Create a viewport
        viewport = new ScreenViewport();
        Camera cam = viewport.getCamera();

        // Create the view
        view = new LayeredView(0.2f, 0.2f, 0.2f);
        battleLayer = new BattleLayer(cam);
        view.add(battleLayer);
    }

    public Camera getCamera() {
        return viewport.getCamera();
    }

    public BattleLayer getBattleLayer() {
        return battleLayer;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        view.resize(width, height);
    }

    public void render(float dt) {
        view.render(dt);
    }

    public void dispose() {
        view.dispose();
    }
}