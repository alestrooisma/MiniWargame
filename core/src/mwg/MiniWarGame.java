package mwg;

import aetherdriven.view.View;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class MiniWarGame extends ApplicationAdapter {
	// Owned
	private View view;

	@Override
	public void create() {
		// Create the view
		view = new View(0.2f, 0.2f, 0.2f);

		// Set up the an input event listener
		Gdx.input.setInputProcessor(new InputHandler());
	}

	@Override
	public void resize(int width, int height) {
		view.resize(width, height);
	}

	@Override
	public void render() {
		view.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
		view.dispose();
	}

	private static class InputHandler extends InputAdapter {

		@Override
		public boolean keyUp(int keycode) {
			switch (keycode) {
				case Keys.ESCAPE:
					Gdx.app.exit();
					return true;
				default:
					return false;
			}
		}
	}
}
