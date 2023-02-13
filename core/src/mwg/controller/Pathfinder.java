package mwg.controller;

import aetherdriven.Maths;
import com.badlogic.gdx.math.Vector2;
import mwg.model.Army;
import mwg.model.GameState;
import mwg.model.Unit;

public class Pathfinder {
    // Not owned
    private final GameState state;
    // Utilities
    private final Vector2 movement = new Vector2();
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

    public void determineMovementDestinationTowards(Unit movingUnit, Unit targetUnit, Vector2 result) {
        determineMovementDestinationTowards(movingUnit, targetUnit.getPosition(), result);
    }

    public void determineMovementDestinationTowards(Unit movingUnit, Vector2 position, Vector2 result) {
        determineMovementDestinationTowards(movingUnit, position.x, position.y, result);
    }

    public Unit determineMovementDestinationTowards(Unit movingUnit, float x, float y, Vector2 result) {
        movement.set(x, y).sub(movingUnit.getPosition());

        // Apply limited movement range
        float limit = movingUnit.getMaxMovement();
        float distance = movement.len();
        if (distance > limit) {
            movement.scl(limit / distance);
        }
        result.set(movement).add(movingUnit.getPosition());

        // Find nearest unit in the moving unit's path
        return determineFirstIntersection(movingUnit, result);
    }

    public Unit determineFirstIntersection(Unit movingUnit, Vector2 result) {
        float minDist2 = Float.MAX_VALUE;
        Unit intersectingUnit = null;

        for (Army army : state.getBattle().getArmies()) {
            for (Unit unit : army.getUnits()) {
                if (unit != movingUnit && isInFrontOf(movingUnit, unit)) {
                    float dist2 = determineIntersection(movingUnit, unit, vec);
                    if (dist2 < movement.len2() && dist2 < minDist2) {
                        minDist2 = dist2;
                        result.set(vec);
                        intersectingUnit = unit;
                    }
                }
            }
        }

        return intersectingUnit;
    }

    public boolean isInFrontOf(Unit movingUnit, Unit unit) {
        vec.set(unit.getPosition()).sub(movingUnit.getPosition());
        return movement.dot(vec) > 0;
    }

    private float determineIntersection(Unit movingUnit, Unit unit, Vector2 result) {
        // Determine coefficients
        float a = movement.y;
        float b = -movement.x;
        float c = a * movingUnit.getPosition().x + b * movingUnit.getPosition().y;
        float x0 = unit.getPosition().x;
        float y0 = unit.getPosition().y;

        // Determine intersections
        int n = Maths.intersection(a, b, c, x0, y0, movingUnit.getRadius() + unit.getRadius());

        // Return nearest intersection
        if (n == 2) {
            float intersection1dist2 = vec.set(Maths.intersection1).sub(movingUnit.getPosition()).len2();
            float intersection2dist2 = vec.set(Maths.intersection2).sub(movingUnit.getPosition()).len2();
            if (intersection1dist2 < intersection2dist2) {
                result.set(Maths.intersection1);
                return intersection1dist2;
            } else {
                result.set(Maths.intersection2);
                return intersection2dist2;
            }
        } else {
            return Float.MAX_VALUE;
        }
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
