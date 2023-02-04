package mwg.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import mwg.controller.events.EventDealer;
import mwg.controller.events.MoveEvent;
import mwg.controller.events.RangedAttackEvent;
import mwg.model.Battle;
import mwg.model.Unit;
import static mwg.controller.BattleController.Interaction.*;

public class BattleController {
    // Owned
    private final EventDealer dealer = new EventDealer();
    private final Pathfinder pathfinder = new Pathfinder();
    // Not owned
    private Battle battle;
    private Unit selected = null;
    private Unit target = null;
    // Utilities
    private final Vector2 destination = new Vector2();

    public Battle getBattle() {
        return battle;
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

    public void setBattle(Battle battle) {
        this.battle = battle;
        pathfinder.setBattle(battle);
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public EventDealer getDealer() {
        return dealer;
    }

    public Interaction determineInteraction(float x, float y, Unit touched) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                && touched != null && !touched.occupies(x, y)) {
            touched = null;
        }

        if (touched != null && touched.getArmy() == battle.getArmies().first()) {
            target = touched;
            return SELECT;
        } else if (selected != null && touched != null) { //TODO check if selected can do ranged attack
            target = touched;
            return RANGED;
        } else if (selected != null) {
            target = pathfinder.determineMovementDestinationTowards(selected, x, y, destination);
            if (pathfinder.isDestinationAvailable(selected, destination)) {
                if (target != null && target.getArmy() != battle.getArmies().first()) {
                    return CHARGE;
                } else {
                    target = null;
                    return MOVE;
                }
            } else {
                target = null;
                return NONE;
            }
        } else {
            target = null;
            return NONE;
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
