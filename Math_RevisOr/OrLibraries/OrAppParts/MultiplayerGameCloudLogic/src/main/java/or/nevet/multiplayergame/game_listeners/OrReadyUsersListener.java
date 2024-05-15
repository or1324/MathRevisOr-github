package or.nevet.multiplayergame.game_listeners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;

public interface OrReadyUsersListener extends Serializable {
    void onUserReady(User user);
    void onAllUsersReady(Users users);
}
