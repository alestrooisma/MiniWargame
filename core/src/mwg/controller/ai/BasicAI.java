package mwg.controller.ai;

import com.badlogic.gdx.utils.Array;
import mwg.controller.Pathfinder;
import mwg.model.Army;
import mwg.model.Battle;
import mwg.model.GameState;
import mwg.model.Unit;

public class BasicAI implements AI {
    // Owned
    private final Pathfinder pathfinder;
    private final Array<Unit> units;
    private final Array<Unit> opponents;
    private final Array<Targeting> targeting;
    // Not owned
    private final Battle battle;
    private final Army thisArmy; //TODO this goes wrong if the battle changes in state

    public BasicAI(GameState state, Army army) {
        this.battle = state.getBattle();
        this.thisArmy = army;
        this.units = new Array<>(army.getUnits().size);
        this.targeting = new Array<>(army.getUnits().size);
        this.opponents = createOpponents();
        this.pathfinder = new Pathfinder(state, targeting);
    }

    private Array<Unit> createOpponents() {
        // Determine required size
        int size = 0;
        for (Army army : battle.getArmies()) {
            if (army != thisArmy) {
                size += army.getUnits().size;
            }
        }

        // Create and fill array
        Array<Unit> opponents = new Array<>(size);
        for (Army army : battle.getArmies()) {
            if (army != thisArmy) {
                opponents.addAll(army.getUnits());
            }
        }
        return opponents;
    }

    @Override
    public void update() {
        targeting.clear();
        units.clear();
        units.addAll(thisArmy.getUnits());

        // Select targets for each unit
        for (Unit unit : units) {
            Unit target = null;
            float minDist2 = Float.MAX_VALUE;

            // Find nearest opponent
            for (Unit opponent : opponents) {
                float dist2 = opponent.getPosition().dst2(unit.getPosition());
                if (dist2 < minDist2) {
                    target = opponent;
                    minDist2 = dist2;
                }
            }

            // Add to targeting array
            targeting.add(new Targeting(unit, target));
        }

        // Choose movement destinations
        for (Targeting t : targeting) {
            pathfinder.determineMovementDestinationTowards(t.unit, t.target, t.destination);
            t.action = Action.MOVE;
        }
    }

    public Array<Targeting> getTargeting() {
        return targeting;
    }
}
