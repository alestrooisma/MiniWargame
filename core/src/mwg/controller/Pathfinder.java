package mwg.controller;

import com.badlogic.gdx.math.Vector2;
import mwg.model.Army;
import mwg.model.Battle;
import mwg.model.Unit;

public class Pathfinder {
    // Not owned
    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Unit getUnitAt(float x, float y) {
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                if (unit.occupies(x, y)) {
                    return unit;
                }
            }
        }
        return null;
    }

    public boolean isDestinationAvailable(Unit movingUnit, Vector2 destination) {
        return isDestinationAvailable(movingUnit, destination.x, destination.y);
    }

    public boolean isDestinationAvailable(Unit movingUnit, float x, float y) {
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                if (unit != movingUnit && unit.overlaps(x, y, movingUnit.getRadius())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void determineMovementDestinationTowards(Unit movingUnit, float x, float y, Vector2 result) {
        Unit nearestUnit = getNearestUnit(x, y);
        if (nearestUnit.occupies(x, y)){
            result.set(nearestUnit.getPosition()).sub(movingUnit.getPosition());
            float dist = result.len() - movingUnit.getRadius() - nearestUnit.getRadius();
            result.nor().scl(dist).add(movingUnit.getPosition());
        } else if (nearestUnit.overlaps(x, y, movingUnit.getRadius())) {
            result.set(x, y).sub(nearestUnit.getPosition());
            float dist = movingUnit.getRadius() + nearestUnit.getRadius();
            result.nor().scl(dist).add(nearestUnit.getPosition());
        } else {
            result.set(x, y);
        }
    }

    public Unit getNearestUnit(float x, float y) {
        return getNearestUnit(x, y, null);
    }

    public Unit getNearestUnit(float x, float y, Unit except) {
        Unit nearest = null;
        float minimumDistance = Float.MAX_VALUE;
        for (Army army : battle.getArmies()) {
            for (Unit unit : army.getUnits()) {
                float distance = unit.getPosition().dst(x, y);
                if (unit != except && distance < minimumDistance) {
                    minimumDistance = distance;
                    nearest = unit;
                }
            }
        }
        return nearest;
    }
}
