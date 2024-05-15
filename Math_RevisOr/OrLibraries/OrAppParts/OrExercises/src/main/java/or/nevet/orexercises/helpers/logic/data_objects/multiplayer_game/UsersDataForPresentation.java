package or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game;

import or.nevet.multiplayergame.data_objects.Users;

public class UsersDataForPresentation {
    private final Users usersDataToPresent;
    private final boolean wasTheGameEnded;

    public UsersDataForPresentation(Users usersDataToPresent, boolean wasTheGameEnded) {
        this.usersDataToPresent = usersDataToPresent;
        this.wasTheGameEnded = wasTheGameEnded;
    }

    public boolean wasTheGameEnded() {
        return wasTheGameEnded;
    }

    public Users getUsersToPresent() {
        return usersDataToPresent;
    }
}
