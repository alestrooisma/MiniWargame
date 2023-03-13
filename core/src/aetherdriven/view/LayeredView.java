package aetherdriven.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class LayeredView implements InputProcessor, Disposable {
    // Owned
    private final Color clearColor;
    private final Array<Layer> layers = new Array<>();

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

    public void add(Layer l) {
        layers.add(l);
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

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().keyDown(keycode);
        }
        return handled;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().keyUp(keycode);
        }
        return handled;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().keyTyped(character);
        }
        return handled;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().touchDown(screenX, screenY, pointer, button);
        }
        return handled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().touchUp(screenX, screenY, pointer, button);
        }
        return handled;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().touchDragged(screenX, screenY, pointer);
        }
        return handled;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().mouseMoved(screenX, screenY);
        }
        return handled;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        boolean handled = false;
        for (int i = layers.size - 1; !handled && i >= 0; i--) {
            handled = layers.get(i).getInputProcessor().scrolled(amountX, amountY);
        }
        return handled;
    }
}
