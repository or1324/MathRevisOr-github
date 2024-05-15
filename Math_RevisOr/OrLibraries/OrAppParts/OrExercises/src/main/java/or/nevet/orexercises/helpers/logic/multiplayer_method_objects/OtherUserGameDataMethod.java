package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public abstract class OtherUserGameDataMethod extends GameDataMethod {

    protected final String myFormattedEmail;
    protected final String ownerFormattedEmail;
    protected final Users allUsersWithMe;

    public OtherUserGameDataMethod(String myFormattedEmail, String ownerFormattedEmail, Users allUsersWithMe, GameFormatter formatter) {
        super(formatter);
        this.myFormattedEmail = myFormattedEmail;
        this.ownerFormattedEmail = ownerFormattedEmail;
        this.allUsersWithMe = allUsersWithMe;
    }

    //the next method is the multiplayer ready method of the other user.
    @Override
    protected MultiplayerReadyMethod getReadyUsersMethodToBeOpenedOnButtonClick() {
        return new MultiplayerOtherUserReadyMethod(allUsersWithMe, myFormattedEmail, ownerFormattedEmail, getFormatter());
    }
}
