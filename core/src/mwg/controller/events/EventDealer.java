package mwg.controller.events;

import com.badlogic.gdx.utils.Array;

public class EventDealer {
    private final Array<EventListener> listeners = new Array<>();

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void deal(Event event) {
        for (EventListener listener : listeners) {
            event.accept(listener);
        }
    }
}
