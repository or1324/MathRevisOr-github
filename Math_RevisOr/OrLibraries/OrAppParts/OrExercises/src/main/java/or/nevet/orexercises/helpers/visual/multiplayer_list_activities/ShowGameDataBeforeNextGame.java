package or.nevet.orexercises.helpers.visual.multiplayer_list_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.multiplayer_game.UsersDataForPresentation;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.CreationGameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.GameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.JoiningGameDataMethod;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;

public class ShowGameDataBeforeNextGame extends ListActivity {

    private GameDataMethod method;
    private ImageButton nextExercise;
    private ImageButton back;
    private AppCompatImageView title;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game_data_before_next_game);
        method = (GameDataMethod) getIntent().getExtras().getSerializable(GeneralExerciseConstants.gameDataMethodExtrasName);
        nextExercise = findViewById(R.id.next_exercise);
        back = findViewById(R.id.back2);
        title = findViewById(R.id.data_title);
        back.setOnClickListener(v -> {
            setResult(GeneralExerciseConstants.showGameDataBeforeNextGameActivityResultCode, null);
            finish();
        });
        nextExercise.setOnClickListener(v -> {
            if (!clicked) {
                clicked = true;
                //opens the ready users list activity to wait for everyone to be ready.
                method.onNextButtonClicked(ShowGameDataBeforeNextGame.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(GeneralExerciseConstants.readyUsersListActivityResultCode, null);
        super.onBackPressed();
    }

    @Override
    public List<CharSequence> generateListItems() {
        UsersDataForPresentation dataForPresentation = method.runMethod();
        CharSequence[] formattedDataToDisplay = null;
        if (dataForPresentation.getUsersToPresent().getClass() == Users.class)
            formattedDataToDisplay = method.getFormatter().formatGameDataReadyUsersInBeginning(dataForPresentation.getUsersToPresent());
        else if (dataForPresentation.getUsersToPresent().getClass() == GameUsers.class) {
            GameUsers gameUsers = (GameUsers) dataForPresentation.getUsersToPresent();
            if (!dataForPresentation.wasTheGameEnded())
                formattedDataToDisplay = method.getFormatter().formatScores(gameUsers);
        }
        if (dataForPresentation.wasTheGameEnded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GeneralExerciseConstants.endGameUsersAndTheirScoreExtrasName, dataForPresentation.getUsersToPresent());
            bundle.putSerializable(GeneralExerciseConstants.formatterInMultiplayerGameExtrasName, method.getFormatter());
            ActivityOpeningHelper.openActivityAndKillMe(this, EndGameActivity.class, bundle, true);
            return new LinkedList<>();
        }
        if (method.getClass() == CreationGameDataMethod.class || method.getClass() == JoiningGameDataMethod.class)
            setMatchingTitle();
        else
            setGameTitle();
        BackgroundRunningHelper.runCodeOnUiThread(() -> nextExercise.setVisibility(View.VISIBLE));
        return new LinkedList<>(Arrays.asList(formattedDataToDisplay));
    }

    private void setGameTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(ShowGameDataBeforeNextGame.this, R.drawable.game_data_title_on_game)));
    }

    private void setMatchingTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(ShowGameDataBeforeNextGame.this, R.drawable.gama_data_title_on_matching)));
    }

    @Override
    public ListFragment getListFragment() {
        ListFragment fragment =  (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment;
    }
}
