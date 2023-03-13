package aetherdriven.view;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;

public interface Layer extends Disposable {
    InputAdapter DEFAULT_INPUT_PROCESSOR = new InputAdapter();

    void resize(int width, int height);

    void update(float dt);

    void render();

    InputProcessor getInputProcessor();
}
