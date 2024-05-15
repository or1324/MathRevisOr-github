package or.nevet.multiplayergame.data_objects;

import java.io.Serializable;

public class Users implements Serializable {
    private final User[] users;
    public Users(User[] users) {
        this.users = users;
    }

    public User[] getUsers() {
        return users;
    }
}
