package mwg.controller.events;

public class ModelEventListener implements EventListener {
    @Override
    public void handleMoveEvent(MoveEvent event) {
        event.getUnit().setPosition(event.getDestination());
    }
}
