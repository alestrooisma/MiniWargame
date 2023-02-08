package mwg.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import mwg.controller.ai.AI;
import mwg.controller.ai.PassiveAI;
import mwg.model.GameState;
import mwg.model.Unit;
import mwg.model.events.EndTurnEvent;
import mwg.model.events.MoveEvent;
import mwg.model.events.RangedAttackEvent;
import mwg.model.events.StartTurnEvent;

public class BattleController {
    // Owned
    private final EventDealer dealer = new EventDealer();
    private final Pathfinder pathfinder;
    private final Array<AI> aiList = new Array<>(2); //TODO where to put this...
    // Not owned
    private final GameState state;
    private Unit selected = null;
    private Unit target = null;
    // Utilities
    private final Vector2 destination = new Vector2();

    public BattleController(GameState state) {
        this.state = state;
        this.pathfinder = new Pathfinder(state);

        aiList.add(null);
        aiList.add(new PassiveAI());
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
        if (state.getCurrentPlayer() != state.getBattle().getArmies().first()) {
            target = null;
            return Interaction.NONE;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                && touched != null && !touched.occupies(x, y)) {
            touched = null;
        }

        if (touched != null && touched.getArmy() == state.getBattle().getArmies().first()) {
            target = touched;
            return Interaction.SELECT;
        } else if (selected != null && touched != null) {
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

    public void endTurn() {
        // Clear any relevant state in this controller
        selected = null;

        // Fire end turn event
        dealer.deal(new EndTurnEvent());

        // Determine next player and round number
        StartTurnEvent startTurnEvent = createStartTurnEvent();

        // Let AIs execute their turn
        while (isAI(startTurnEvent.getPlayerIndex())) {
            performAiTurn(startTurnEvent);

            // Update startTurnEvent after the AI's turn is done
            startTurnEvent = createStartTurnEvent();
        }

        // Fire event for new player turn
        dealer.deal(startTurnEvent);
    }

    private StartTurnEvent createStartTurnEvent() {
        // Determine next player
        int nextPlayerIndex = (state.getCurrentPlayerIndex() + 1) % state.getBattle().getArmies().size;

        // Update round number if required
        int round = state.getRound();
        if (nextPlayerIndex == 0) {
            round++;
        }

        // Return a StartTurnEvent
        return new StartTurnEvent(nextPlayerIndex, round);
    }

    private boolean isAI(int playerIndex) {
        return aiList.get(playerIndex) != null;
    }

    private void performAiTurn(StartTurnEvent startTurnEvent) {
        // Start the turn
        dealer.deal(startTurnEvent);

        // Run the AI
        AI ai = aiList.get(startTurnEvent.getPlayerIndex());
        ai.update();
        //TODO fire events for AI's actions

        // End the turn
        dealer.deal(new EndTurnEvent());
    }

    public enum Interaction {
        NONE, SELECT, MOVE, CHARGE, RANGED
    }
}
