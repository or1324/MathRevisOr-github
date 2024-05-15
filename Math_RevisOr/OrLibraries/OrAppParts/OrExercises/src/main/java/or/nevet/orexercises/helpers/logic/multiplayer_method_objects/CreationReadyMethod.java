package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import java.io.Serializable;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
public class CreationReadyMethod extends ReadyUsersMethod {
    private final int playersNum;
    private final char sign;
    private final String myFormattedEmail;

    public CreationReadyMethod(int playersNum, char sign, String myFormattedEmail, GameFormatter formatter) {
        super(formatter);
        this.playersNum = playersNum;
        this.sign = sign;
        this.myFormattedEmail = myFormattedEmail;
    }

    @Override
    public Serializable runMethod(OrReadyUsersListener listener) {
        GameOperations.createRoom(myFormattedEmail, playersNum, sign, listener);
        return new Serializable() {};
    }
}
