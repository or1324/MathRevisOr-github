package or.nevet.orexercises.helpers.logic.multiplayer_method_objects;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import java.io.Serializable;

import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ReadyUsersListActivity;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;

public abstract class GameDataMethod implements Serializable {

    private final GameFormatter formatter;

    public GameDataMethod(GameFormatter formatter) {
        this.formatter = formatter;
    }

    public GameFormatter getFormatter() {
        return formatter;
    }

    protected abstract MultiplayerReadyMethod getReadyUsersMethodToBeOpenedOnButtonClick();

    //opens the ready users list activity from the gameDataActivity to wait for everyone to be ready.
    public void onNextButtonClicked(MusicActivity activity) {
        ReadyUsersMethod method = getReadyUsersMethodToBeOpenedOnButtonClick();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GeneralExerciseConstants.readyUsersMethodExtrasName, method);
        ActivityOpeningHelper.openActivityAndKillMe(activity, ReadyUsersListActivity.class, bundle, true);
    }

    public abstract UsersDataForPresentation runMethod();
}
