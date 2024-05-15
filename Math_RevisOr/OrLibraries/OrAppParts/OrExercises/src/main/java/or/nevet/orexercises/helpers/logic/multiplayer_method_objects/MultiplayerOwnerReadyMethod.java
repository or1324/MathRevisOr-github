package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import java.io.Serializable;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public class MultiplayerOwnerReadyMethod extends MultiplayerReadyMethod {

    private final Users allUsersWithMe;
    private final String myFormattedEmail;
    public MultiplayerOwnerReadyMethod(Users allUsersWithMe, String myFormattedEmail, GameFormatter formatter) {
        super(formatter);
        this.allUsersWithMe = allUsersWithMe;
        this.myFormattedEmail = myFormattedEmail;
    }

    @Override
    public Serializable runMethod(OrReadyUsersListener listener) {
        GameOperations.getReadyToPlayAsRoomOwner(myFormattedEmail, allUsersWithMe, listener);
        return new Serializable() {};
    }
}
