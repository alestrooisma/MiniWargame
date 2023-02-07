package mwg.controller;

import com.badlogic.gdx.math.Vector2;
import mwg.model.Army;
import mwg.model.GameState;
import mwg.model.Unit;

public class Pathfinder {
    // Not owned
    private final GameState state;
    // Utilities
    private final Vector2 vec = new Vector2();

    public Pathfinder(GameState state) {
        this.state = state;
    }

    public Unit getUnitAt(float x, float y) {
        for (Army army : state.getBattle().getArmies()) {
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
        for (Army army : state.getBattle().getArmies()) {
            for (Unit unit : army.getUnits()) {
                if (unit != movingUnit && unit.overlaps(x, y, movingUnit.getRadius())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Unit determineMovementDestinationTowards(Unit movingUnit, float x, float y, Vector2 result) {
        // Apply limited movement range
        float limit = movingUnit.getMaxMovement();
        float dist2 = vec.set(x, y).sub(movingUnit.getPosition()).len2();
        if (dist2 > limit*limit) {
            vec.nor().scl(limit).add(movingUnit.getPosition());
            x = vec.x;
            y = vec.y;
        }

        // Determine exact destination in case area is occupied
        Unit nearestUnit = getNearestUnit(x, y, movingUnit);
        if (nearestUnit.overlaps(x, y, movingUnit.getRadius())) {
            result.set(x, y).sub(nearestUnit.getPosition());
            float dist = movingUnit.getRadius() + nearestUnit.getRadius();
            result.nor().scl(dist).add(nearestUnit.getPosition());
        } else {
            result.set(x, y);
            nearestUnit = null;
        }
        return nearestUnit;
    }

    public Unit getNearestUnit(float x, float y) {
        return getNearestUnit(x, y, null);
    }

    public Unit getNearestUnit(float x, float y, Unit except) {
        Unit nearest = null;
        float minimumDistance = Float.MAX_VALUE;
        for (Army army : state.getBattle().getArmies()) {
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
