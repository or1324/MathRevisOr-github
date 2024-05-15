package or.nevet.multiplayergame.data_objects;

public class GameUsers extends Users {
    public GameUsers(GameUser[] users) {
        super(users);
    }

    @Override
    public GameUser[] getUsers() {
        return (GameUser[]) super.getUsers();
    }
}
