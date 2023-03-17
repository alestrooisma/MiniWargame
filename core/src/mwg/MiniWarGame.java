package mwg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import mwg.controller.BattleController;
import mwg.controller.ai.AI;
import mwg.controller.ai.BasicAI;
import mwg.model.Army;
import mwg.model.Battle;
import mwg.model.GameState;
import mwg.model.Unit;
import mwg.model.UnitType;
import mwg.model.events.ModelEventListener;
import mwg.model.events.StartTurnEvent;
import mwg.view.BattleLayer;
import mwg.view.Element;
import mwg.view.ResourceContainer;
import mwg.view.Skin;
import mwg.view.View;

public class MiniWarGame extends ApplicationAdapter {
    // Owned
    private ResourceContainer resources;
    private View view;
    private Array<AI> aiList;

    @Override
    public void create() {
        // Create model
        UnitType spearman = new UnitType("spearman");

        float cx = 30;
        float cy = 30;
        Army player = new Army(3);
        player.add(new Unit(spearman, "Unit 1", player, cx-11.6f, cy-16.8f));
        player.add(new Unit(spearman, "Unit 2", player, cx-11.2f, cy-12.8f));
        player.add(new Unit(spearman, "Unit 3", player, cx-9.6f, cy-17.6f));

        Army opponent = new Army(3);
        opponent.add(new Unit(spearman, "Enemy 1", opponent, cx+11.6f, cy+13.2f));
        opponent.add(new Unit(spearman, "Enemy 2", opponent, cx+9f, cy+13.6f));
        opponent.add(new Unit(spearman, "Enemy 3", opponent, cx+10.8f, cy+10.4f));

        Battle battle = new Battle(cx-20, cy-20, 40, 40);
        battle.add(player);
        battle.add(opponent);

        // Initialize game state
        GameState state = new GameState();
        state.setBattle(battle);

        // Set up the AI
        aiList = new Array<>(2);
        aiList.add(null);
        aiList.add(new BasicAI(state, state.getBattle().getArmies().get(1)));

        // Create the controller
        BattleController controller = new BattleController(state, aiList);

        // Create the view
        view = new View(controller);

        // Prepare resources
        resources = ResourceContainer.create();
        Texture texture = new Texture(Gdx.files.internal("spearman.png"));
        Skin skin = new Skin(texture, 37, 17, new Rectangle(-16, -3, 30, 42));
        resources.add(spearman.getName(), skin);

        // Populate Battle Layer
        BattleLayer battleLayer = view.getBattleLayer();
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                battleLayer.add(new Element(unit));
            }
        }
        battleLayer.setPlayerArmy(player);

        // Set up the event system
        controller.getDealer().register(new ModelEventListener(state));
        controller.getDealer().register(battleLayer);
        controller.getDealer().register(view.getDebugLayer());

        // Set up an input event listener
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(view.getView());
        inputMultiplexer.addProcessor(new InputHandler());
        Gdx.input.setInputProcessor(inputMultiplexer);

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
        resources.dispose();
    }

    private class InputHandler extends InputAdapter {
        // Owned
        private int aiNumber = 0;

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Keys.F11:
                    aiNumber = (aiNumber + 1) % aiList.size;
                    view.getAiLayer().setAi(aiList.get(aiNumber));
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
