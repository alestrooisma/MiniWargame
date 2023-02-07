package mwg.controller;

import com.badlogic.gdx.utils.Array;
import mwg.model.events.Event;
import mwg.model.events.EventListener;

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
