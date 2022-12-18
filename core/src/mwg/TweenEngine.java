package mwg;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TweenEngine {
    private final Array<TweenAction> actions = new Array<>();

    public void add(Vector2 target, float x, float y, float speed) {
        actions.add(new TweenAction(target, x, y, speed));
    }

    public void update(float dt) {
        for (TweenAction action : actions) {
            boolean done = action.update(dt);
            if (done) {
                actions.removeValue(action, true);
            }
        }
    }

    public static class TweenAction {
        // Owned
        private final Vector2 destination = new Vector2();
        private final Vector2 velocity = new Vector2();
        // Not owned
        private final Vector2 target;

        public TweenAction(Vector2 target, float x, float y, float speed) {
            this.target = target;
            this.destination.set(x, y);
            this.velocity.set(x, y).sub(target).nor().scl(speed);
        }

        public boolean update(float dt) {
            if (Math.abs(velocity.x * dt) < Math.abs(destination.x - target.x)) {
                target.add(velocity.x * dt, velocity.y * dt);
                return false;
            } else {
                target.set(destination);
                return true;
            }
        }
    }
}
