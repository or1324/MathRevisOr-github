package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import java.io.Serializable;

import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public abstract class ReadyUsersMethod implements Serializable {
    private final GameFormatter formatter;
    public ReadyUsersMethod(GameFormatter formatter) {
        this.formatter = formatter;
    }
    public abstract Serializable runMethod(OrReadyUsersListener listener);

    public String formatReadyUser(User user) {
        return formatter.formatReadyUser(user);
    }
}
