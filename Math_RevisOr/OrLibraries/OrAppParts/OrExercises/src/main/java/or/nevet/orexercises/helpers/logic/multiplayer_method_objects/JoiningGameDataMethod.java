package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public class JoiningGameDataMethod extends OtherUserGameDataMethod {

    private final Users allUsersWithoutMe;
    public JoiningGameDataMethod(Users allUsersWithoutMe, String myEmail, String ownerEmail, Users allEmailsWithMine, GameFormatter formatter) {
        super(myEmail, ownerEmail, allEmailsWithMine, formatter);
        this.allUsersWithoutMe = allUsersWithoutMe;
    }

    @Override
    public UsersDataForPresentation runMethod() {
        return new UsersDataForPresentation(allUsersWithoutMe, false);
    }
}
