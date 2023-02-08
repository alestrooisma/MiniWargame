package mwg.model.events;

public class StartTurnEvent extends Event {
    private final int playerIndex;

    public StartTurnEvent(int playerIndex) {
        super(null);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleStartTurnEvent(this);
    }
}
