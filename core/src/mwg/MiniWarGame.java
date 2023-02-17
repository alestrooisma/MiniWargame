package mwg;

import aetherdriven.Maths;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import mwg.controller.BattleController;
import mwg.model.GameState;
import mwg.model.events.ModelEventListener;
import mwg.model.Army;
import mwg.model.Battle;
import mwg.model.Unit;
import mwg.model.events.StartTurnEvent;
import mwg.view.BattleLayer;
import mwg.view.Element;
import mwg.view.Skin;
import mwg.view.View;

public class MiniWarGame extends ApplicationAdapter {
    // Owned
    private BattleController controller;
    private View view;
    private Skin skin;

    @Override
    public void create() {
        // Create model
        Army player = new Army(3);
        player.add(new Unit("Unit 1", player, 30/25f, 60/25f));
        player.add(new Unit("Unit 2", player, 40/25f, 160/25f));
        player.add(new Unit("Unit 3", player, 80/25f, 40/25f));

        Army opponent = new Army(3);
        opponent.add(new Unit("Enemy 1", opponent, 610/25f, 810/25f));
        opponent.add(new Unit("Enemy 2", opponent, 545/25f, 820/25f));
        opponent.add(new Unit("Enemy 3", opponent, 590/25f, 740/25f));

        Battle battle = new Battle();
        battle.add(player);
        battle.add(opponent);

        // Initialize game state
        GameState state = new GameState();
        state.setBattle(battle);

        // Create the controller
        controller = new BattleController(state);

        // Create the view
        view = new View(controller);

        // Populate Battle Layer
        BattleLayer battleLayer = view.getBattleLayer();
        Texture texture = new Texture(Gdx.files.internal("spearman.png"));
        skin = new Skin(texture, 37, 17, new Rectangle(-16, -3, 30, 42));
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                battleLayer.add(new Element(unit, skin));
            }
        }
        battleLayer.setPlayerArmy(player);

        // Set up the event system
        controller.getDealer().register(new ModelEventListener(state));
        controller.getDealer().register(battleLayer);
        controller.getDealer().register(view.getDebugLayer());

        // Set up an input event listener
        Gdx.input.setInputProcessor(new InputHandler());

        // Start the first turn
        controller.getDealer().deal(new StartTurnEvent(0, 1));
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

    private class InputHandler extends InputAdapter {
        // Utilities
        private final Vector3 vec = new Vector3();

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                vec.set(screenX, screenY, 0);
                view.getCamera().unproject(vec);
                view.getBattleLayer().touch(button, vec.x, vec.y);
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Keys.ENTER:
                    controller.endTurn();
                    return true;
                case Keys.F12:
                    view.getDebugLayer().toggle();
                    return true;
                case Keys.ESCAPE:
                    Gdx.app.exit();
                    return true;
                default:
                    return false;
            }
        }
    }
}
