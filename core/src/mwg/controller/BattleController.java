package mwg.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import mwg.model.GameState;
import mwg.model.Unit;
import mwg.model.events.MoveEvent;
import mwg.model.events.RangedAttackEvent;

public class BattleController {
    // Owned
    private final EventDealer dealer = new EventDealer();
    private final Pathfinder pathfinder;
    // Not owned
    private final GameState state;
    private Unit selected = null;
    private Unit target = null;
    // Utilities
    private final Vector2 destination = new Vector2();

    public BattleController(GameState state) {
        this.state = state;
        this.pathfinder = new Pathfinder(state);
    }

    public Unit getSelected() {
        return selected;
    }

    public Unit getTarget() {
        return target;
    }

    public Vector2 getDestination() {
        return destination;
    }

    public EventDealer getDealer() {
        return dealer;
    }

    public Interaction determineInteraction(float x, float y, Unit touched) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                && touched != null && !touched.occupies(x, y)) {
            touched = null;
        }

        if (touched != null && touched.getArmy() == state.getBattle().getArmies().first()) {
            target = touched;
            return Interaction.SELECT;
        } else if (selected != null && touched != null) { //TODO check if selected can do ranged attack
            target = touched;
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || !state.mayPerformRangedAttack(selected)) {
                return determineMovementInteraction(x, y);
            } else {
                return Interaction.RANGED;
            }
        } else if (selected != null) {
            return determineMovementInteraction(x, y);
        } else {
            target = null;
            return Interaction.NONE;
        }
    }

    private Interaction determineMovementInteraction(float x, float y) {
        if (!state.mayMove(selected)) {
            target = null;
            return Interaction.NONE;
        }

        target = pathfinder.determineMovementDestinationTowards(selected, x, y, destination);
        if (pathfinder.isDestinationAvailable(selected, destination)) {
            if (target != null && target.getArmy() != state.getBattle().getArmies().first()) {
                return Interaction.CHARGE;
            } else {
                target = null;
                return Interaction.MOVE;
            }
        } else {
            target = null;
            return Interaction.NONE;
        }
    }

    public void interact(float x, float y, Unit touched) {
        Interaction interaction = determineInteraction(x, y, touched);
        switch (interaction) {
            case SELECT:
                selected = touched;
                break;
            case MOVE:
            case CHARGE:
                dealer.deal(new MoveEvent(selected, destination));
                break;
            case RANGED:
                dealer.deal(new RangedAttackEvent(selected, touched));
                break;
        }
    }

    public void cancel() {
        selected = null;
    }

    public enum Interaction {
        NONE, SELECT, MOVE, CHARGE, RANGED
    }
}
