package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ReadyUsersListActivity;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;

public abstract class OwnerGameDataMethod extends GameDataMethod {

    protected final LearnedExercise exercise;
    protected final String myFormattedEmail;
    protected final Users allUsersWithMe;

    public OwnerGameDataMethod(LearnedExercise exercise, String myFormattedEmail, Users allUsersWithMe, GameFormatter formatter) {
        super(formatter);
        this.exercise = exercise;
        this.myFormattedEmail = myFormattedEmail;
        this.allUsersWithMe = allUsersWithMe;
    }

    //the next method is the multiplayer ready method of the owner.
    @Override
    protected MultiplayerReadyMethod getReadyUsersMethodToBeOpenedOnButtonClick() {
        return new MultiplayerOwnerReadyMethod(allUsersWithMe, myFormattedEmail, getFormatter());
    }
}
