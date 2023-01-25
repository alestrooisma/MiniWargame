package mwg.controller.events;

public interface Event {
    void accept(EventListener listener);
}
