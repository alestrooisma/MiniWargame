package mwg.model;

import com.badlogic.gdx.math.Vector2;

public class Unit {
    // Owned
    private final UnitType type;
    private final String name;
    private final Army army;
    private final Vector2 position;
    private final float radius;

    public Unit(UnitType type, String name, Army army, float x, float y) {
        this(type, name, army, new Vector2(x, y));
    }

    public Unit(UnitType type, String name, Army army, Vector2 position) {
        this(type, name, army, position, 1);
    }

    public Unit(UnitType type, String name, Army army, Vector2 position, float radius) {
        this.type = type;
        this.name = name;
        this.army = army;
        this.position = position;
        this.radius = radius;
    }

    public UnitType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Army getArmy() {
        return army;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public float getRadius() {
        return radius;
    }

    public boolean occupies(float x, float y) {
        float dx = x - position.x;
        float dy = y - position.y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean overlaps(float x, float y, float r) {
        float dx = x - position.x;
        float dy = y - position.y;
        float minimum = radius + r;
        return dx*dx + dy*dy < minimum * minimum * (1 - 1e-5f); // Accommodate for floating point errors
    }

    public float getMaxMovement() {
        return 10;
    }
}
