package mwg.model.events;

public class StartTurnEvent extends Event {
    private final int playerIndex;
    private final int round;

    public StartTurnEvent(int playerIndex, int round) {
        super(null);
        this.playerIndex = playerIndex;
        this.round = round;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getRound() {
        return round;
    }

    @Override
    public void accept(EventListener listener) {
        listener.handleStartTurnEvent(this);
    }
}
