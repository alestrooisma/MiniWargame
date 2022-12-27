package mwg.model;

import com.badlogic.gdx.math.Vector2;

public class Unit {
    // Owned
    private final Army army;
    private final Vector2 position;
    private final float radius;

    public Unit(Army army, float x, float y) {
        this(army, new Vector2(x, y));
    }

    public Unit(Army army, Vector2 position) {
        this(army, position, 26);
    }

    public Unit(Army army, Vector2 position, float radius) {
        this.army = army;
        this.position = position;
        this.radius = radius;
    }

    public Army getArmy() {
        return army;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public float getRadius() {
        return radius;
    }

    public boolean occupies(float x, float y) {
        float dx = x - position.x;
        float dy = (y - position.y) * 2; // multiplied by two to correct for isometric perspective
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean overlaps(float x, float y, float r) {
        float dx = x - position.x;
        float dy = (y - position.y) * 2; // multiplied by two to correct for isometric perspective
        float minimum = radius + r;
        return dx*dx + dy*dy < minimum * minimum;
    }
}
