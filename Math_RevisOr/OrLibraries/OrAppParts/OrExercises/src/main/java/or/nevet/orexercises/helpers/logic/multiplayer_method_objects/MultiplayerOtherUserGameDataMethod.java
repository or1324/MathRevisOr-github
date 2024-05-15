package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.GameExerciseResult;
import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;

public class MultiplayerOtherUserGameDataMethod extends OtherUserGameDataMethod {

    private final long score;
    public MultiplayerOtherUserGameDataMethod(long score, String myFormattedEmail, String ownerFormattedEmail, Users allEmailsWithMine, GameFormatter formatter) {
        super(myFormattedEmail, ownerFormattedEmail, allEmailsWithMine, formatter);
        this.score = score;
    }

    @Override
    public UsersDataForPresentation runMethod() {
        GameExerciseResult result = GameOperations.finishCurrentExerciseAsOtherUserAndGetAllScores(myFormattedEmail, ownerFormattedEmail, score);
        GameUsers usersAndScores = result.getUsers();
        boolean wasGameEnded = result.wasTheGameEnded();
        return new UsersDataForPresentation(usersAndScores, wasGameEnded);
    }
}
