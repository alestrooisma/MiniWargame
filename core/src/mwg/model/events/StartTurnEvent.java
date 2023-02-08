package mwg.model.events;

public class StartTurnEvent extends Event {

    public StartTurnEvent() {
        super(null);
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleStartTurnEvent(this);
    }
}
