package mwg.model.events;

public class EndTurnEvent extends Event {

    public EndTurnEvent() {
        super(null);
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleEndTurnEvent(this);
    }
}
