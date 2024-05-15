package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.GameExerciseResult;
import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;

public class MultiplayerOwnerGameDataMethod extends OwnerGameDataMethod {

    private final long score;
    private final Users allUsersWithMe;

    public MultiplayerOwnerGameDataMethod(LearnedExercise exercise, long score, String myEmail, Users allUsersWithMe, GameFormatter formatter) {
        super(exercise, myEmail, allUsersWithMe, formatter);
        this.score = score;
        this.allUsersWithMe = allUsersWithMe;
    }

    @Override
    public UsersDataForPresentation runMethod() {
        LearnedExerciseData learnedExerciseData = new LearnedExerciseData(exercise.getLeftNumber(), exercise.getRightNumber(), 0, exercise.getResult());
        GameExerciseResult result = GameOperations.finishCurrentExerciseAsOwnerAndGetAllScores(myFormattedEmail, learnedExerciseData, score, allUsersWithMe);
        GameUsers usersAndScores = result.getUsers();
        boolean wasGameEnded = result.wasTheGameEnded();
        return new UsersDataForPresentation(usersAndScores, wasGameEnded);
    }
}
