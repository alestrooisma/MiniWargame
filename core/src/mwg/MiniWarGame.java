package mwg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import mwg.model.Army;
import mwg.model.Battle;
import mwg.model.Unit;
import mwg.view.BattleLayer;
import mwg.view.Element;
import mwg.view.Skin;
import mwg.view.View;

public class MiniWarGame extends ApplicationAdapter {
    // Owned
    private View view;
    private Skin skin;

    @Override
    public void create() {
        // Create model
        Army player = new Army(3);
        player.add(new Unit(player, 30, 30));
        player.add(new Unit(player, 40, 80));
        player.add(new Unit(player, 70, 20));

        Army opponent = new Army(3);
        opponent.add(new Unit(opponent, 610, 405));
        opponent.add(new Unit(opponent, 545, 410));
        opponent.add(new Unit(opponent, 600, 360));

        Battle battle = new Battle();
        battle.add(player);
        battle.add(opponent);

        // Create the view
        view = new View();

        // Populate Battle Layer
        BattleLayer battleLayer = view.getBattleLayer();
        Texture texture = new Texture(Gdx.files.internal("spearman.png"));
        skin = new Skin(texture, 37, 17, new Rectangle(-16, -4, 30, 42));
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                battleLayer.add(new Element(unit, skin));
            }
        }
        battleLayer.setPlayerArmy(player);

        // Set up an input event listener
        Gdx.input.setInputProcessor(new InputHandler(view.getCamera(), battleLayer));
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
        skin.dispose();
    }

    private static class InputHandler extends InputAdapter {
        // Not owned
        private final Camera cam;
        private final BattleLayer battleLayer;
        // Utilities
        private final Vector3 vec = new Vector3();

        public InputHandler(Camera cam, BattleLayer battleLayer) {
            this.cam = cam;
            this.battleLayer = battleLayer;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                vec.set(screenX, screenY, 0);
                cam.unproject(vec);
                battleLayer.touch(button, vec.x, vec.y);
            }
            return true;
        }

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
