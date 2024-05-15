package or.nevet.multiplayergame.data_objects;

public class GameExerciseResult {
    private GameUsers users;
    private boolean wasTheGameEnded;
    public GameExerciseResult(GameUsers users, boolean wasTheGameEnded) {
        this.users = users;
        this.wasTheGameEnded = wasTheGameEnded;
    }

    public GameUsers getUsers() {
        return users;
    }

    public boolean wasTheGameEnded() {
        return wasTheGameEnded;
    }
}
