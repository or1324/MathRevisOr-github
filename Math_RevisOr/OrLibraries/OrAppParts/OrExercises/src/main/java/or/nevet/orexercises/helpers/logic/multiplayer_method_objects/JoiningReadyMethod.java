package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import java.io.Serializable;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public class JoiningReadyMethod extends ReadyUsersMethod {

    private final String ownerFormattedEmail;
    private final String myFormattedEmail;

    public JoiningReadyMethod(String ownerFormattedEmail, String myFormattedEmail, GameFormatter formatter) {
        super(formatter);
        this.ownerFormattedEmail = ownerFormattedEmail;
        this.myFormattedEmail = myFormattedEmail;
    }

    @Override
    public Serializable runMethod(OrReadyUsersListener listener) {
        return GameOperations.joinRoom(myFormattedEmail, ownerFormattedEmail, listener);
    }
}
