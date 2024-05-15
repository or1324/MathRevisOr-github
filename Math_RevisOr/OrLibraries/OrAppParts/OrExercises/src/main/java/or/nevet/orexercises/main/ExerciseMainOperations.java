package or.nevet.orexercises.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;

//All activities that are initiated from here need exercises as input.
public class ExerciseMainOperations {

    public enum ExerciseActivityOption {
        PracticeExerciseActivityWithScore,
        PracticeExerciseActivityWithoutScore,
        ExerciseStatsTableActivity
    }

    //Also deals with music among other things
    public static void openActivityAndSendExercises(MusicActivity source, ExerciseActivityOption option, LearnedExercises exercises) {
        Bundle extras = new Bundle();
        extras.putParcelable(GeneralExerciseConstants.exercisesExtrasName, exercises);
        Class<? extends Activity> activityClass;
        switch (option) {
            case PracticeExerciseActivityWithScore:
                activityClass = PracticeExerciseActivityWithScore.class;
                break;
            case PracticeExerciseActivityWithoutScore:
                activityClass = PracticeExerciseActivityWithoutScore.class;
                break;
            case ExerciseStatsTableActivity:
                activityClass = TableActivity.class;
                break;
            default:
                throw new RuntimeException(UserMessagesConstants.forgotAfterAddingExerciseActivity);
        }
        ActivityOpeningHelper.openActivity(source, activityClass, extras);
    }

    public static void openMultiplayerExerciseActivity(MusicActivity source, LearnedExercises exercises, String ownerEmail, char exercisesSign, Users allUsersWithMe, Users allUsersWithoutMe, boolean isOwner, LearnedExercise currentExercise, GameFormatter formatter) {
        Bundle extras = new Bundle();
        extras.putSerializable(GeneralExerciseConstants.currentExerciseMultiplayerGameExtrasName, currentExercise);
        extras.putParcelable(GeneralExerciseConstants.exercisesExtrasName, exercises);
        extras.putString(GeneralExerciseConstants.ownerEmailInMultiplayerGameExtrasName, ownerEmail);
        extras.putChar(GeneralExerciseConstants.exercisesSignInMultiplayerGameExtrasName, exercisesSign);
        extras.putSerializable(GeneralExerciseConstants.usersInMultiplayerGameExtrasName, allUsersWithMe);
        extras.putSerializable(GeneralExerciseConstants.usersWithoutMeInMultiplayerGameExtrasName, allUsersWithoutMe);

        extras.putBoolean(GeneralExerciseConstants.isMultiplayerGameOwnerExtrasName, isOwner);
        extras.putSerializable(GeneralExerciseConstants.formatterInMultiplayerGameExtrasName, formatter);
        ActivityOpeningHelper.openActivity(source, MultiplayerExerciseActivity.class, extras);
    }

    public static void openExerciseCreationTable(MusicActivity source, char sign, int numOfRows, int numOfColumns, boolean isEditing, boolean forwardTheResultToMyParentAndKillMe) {
        Bundle extras = new Bundle();
        extras.putChar(GeneralExerciseConstants.exerciseSignExtrasName, sign);
        extras.putBoolean(GeneralExerciseConstants.isExerciseEditingExtrasName, isEditing);
        extras.putInt(GeneralExerciseConstants.numOfRowsExtrasName, numOfRows);
        extras.putInt(GeneralExerciseConstants.numOfColumnsExtrasName, numOfColumns);
        if (forwardTheResultToMyParentAndKillMe)
            ActivityOpeningHelper.openActivityAndKillMe(source, ExerciseCreationTableActivity.class, extras, true);
        else
            ActivityOpeningHelper.openActivity(source, ExerciseCreationTableActivity.class, extras);
    }

}
