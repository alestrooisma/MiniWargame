package aetherdriven.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import java.util.LinkedList;

public class LayeredView implements Disposable {
    // Owned
    private final Color clearColor;
    private final LinkedList<Layer> layers = new LinkedList<>();

    public LayeredView() {
        this(0, 0, 0);
    }

    public LayeredView(float r, float g, float b) {
        clearColor = new Color(r, g, b, 1);
    }

    public void setClearColor(float r, float g, float b) {
        clearColor.r = r;
        clearColor.g = g;
        clearColor.b = b;
    }

    public boolean add(Layer l) {
        return layers.add(l);
    }

    public boolean remove(Layer l) {
        return layers.remove(l);
    }

    public void clearLayers() {
        layers.clear();
    }

    public void resize(int width, int height) {
        for (Layer layer : layers) {
            layer.resize(width, height);
        }
    }

    public void render(float dt) {
        // Clear the screen
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and render layers
        for (Layer layer : layers) {
            layer.update(dt);
            layer.render();
        }
    }

    @Override
    public void dispose() {
        for (Layer layer : layers) {
            layer.dispose();
        }
    }
}
