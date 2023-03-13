package mwg.view;

import aetherdriven.view.Layer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;

public class MapLayer implements Layer {
    // Owned
    private final HexagonalTiledMapRenderer renderer;
    // Not owned
    private final OrthographicCamera camera;

    public MapLayer(OrthographicCamera camera, TiledMap map) {
        this.renderer = new HexagonalTiledMapRenderer(map);
        this.camera = camera;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        camera.update(); // First layer to be rendered, so updating the camera here...
        renderer.setView(camera);
        renderer.render();
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
