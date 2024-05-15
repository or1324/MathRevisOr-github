package or.nevet.multiplayergame.game_listeners;

import or.nevet.orcloud.listener_stoppers.ValueEventListenerStopper;

public class ExerciseEndingNotifier {
    private final ValueEventListenerStopper stopper;

    public ExerciseEndingNotifier(ValueEventListenerStopper stopper) {
        this.stopper = stopper;
    }

    public void exerciseEnded() {
        stopper.removeListener();
    }
}
