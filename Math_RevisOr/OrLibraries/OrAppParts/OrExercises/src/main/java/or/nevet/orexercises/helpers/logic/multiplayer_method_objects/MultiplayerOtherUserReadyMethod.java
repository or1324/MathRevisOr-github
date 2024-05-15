package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import java.io.Serializable;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public class MultiplayerOtherUserReadyMethod extends MultiplayerReadyMethod {

    private final Users allUsersWithMe;
    private final String myFormattedEmail;
    private final String ownerFormattedEmail;
    public MultiplayerOtherUserReadyMethod(Users allUsersWithMe, String myFormattedEmail, String ownerFormattedEmail, GameFormatter formatter) {
        super(formatter);
        this.allUsersWithMe = allUsersWithMe;
        this.myFormattedEmail = myFormattedEmail;
        this.ownerFormattedEmail = ownerFormattedEmail;
    }

    @Override
    public Serializable runMethod(OrReadyUsersListener listener) {
        return GameOperations.getReadyToPlayAsOtherUser(myFormattedEmail, ownerFormattedEmail, allUsersWithMe, listener);
    }
}
