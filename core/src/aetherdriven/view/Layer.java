package aetherdriven.view;

import com.badlogic.gdx.utils.Disposable;

public interface Layer extends Disposable {

    void resize(int width, int height);

    void update(float dt);

    void render();
}
