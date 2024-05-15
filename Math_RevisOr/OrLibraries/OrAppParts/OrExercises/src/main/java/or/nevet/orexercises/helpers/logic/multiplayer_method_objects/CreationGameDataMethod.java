package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;

public class CreationGameDataMethod extends OwnerGameDataMethod {

    private final LearnedExercise exercise;
    private final Users allUsers;
    private final Users allUsersWithoutMe;
    public CreationGameDataMethod(LearnedExercise exercise, Users allUsers, Users allUsersWithoutMe, String myEmail, GameFormatter formatter) {
        super(exercise, myEmail, allUsers, formatter);
        this.exercise = exercise;
        this.allUsers = allUsers;
        this.allUsersWithoutMe = allUsersWithoutMe;
    }

    @Override
    public UsersDataForPresentation runMethod() {
        GameOperations.initializeGame(getFormatter().getMyFormattedIdentifier(), new LearnedExerciseData(exercise.getLeftNumber(), exercise.getRightNumber(), 0, exercise.getResult()), allUsers);
        return new UsersDataForPresentation(allUsersWithoutMe, false);
    }
}
