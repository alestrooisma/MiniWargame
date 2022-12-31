package mwg.view;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TweenEngine {
    private final Array<TweenAction> actions = new Array<>();

    public void add(Vector3 target, float x, float y, float speed) {
        actions.add(new TweenAction(target, x, y, speed));
    }

    public void add(Vector3 target, Vector3 destination, float speed) {
        add(target, destination.x, destination.y, speed);
    }

    public void update(float dt) {
        for (TweenAction action : actions) {
            boolean done = action.update(dt);
            if (done) {
                actions.removeValue(action, true);
            }
        }
    }

    public boolean isIdle() {
        return actions.isEmpty();
    }

    public boolean isBusy() {
        return actions.notEmpty();
    }

    public static class TweenAction {
        // Owned
        private final Vector3 destination = new Vector3();
        private final Vector3 velocity = new Vector3();
        // Not owned
        private final Vector3 target;

        public TweenAction(Vector3 target, float x, float y, float speed) {
            this.target = target;
            this.destination.set(x, y, 0);
            this.velocity.set(x, y, 0).sub(target).nor().scl(speed);
        }

        public boolean update(float dt) {
            if (Math.abs(velocity.x * dt) < Math.abs(destination.x - target.x)) {
                target.add(velocity.x * dt, velocity.y * dt, 0);
                return false;
            } else {
                target.set(destination);
                return true;
            }
        }
    }
}
