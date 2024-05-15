package or.nevet.orexercises.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.AllVSAllFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GroupsFormatter;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.CreationGameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.CreationReadyMethod;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ReadyUsersListActivity;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ShowGameDataBeforeNextGame;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.activity_types.ButtonsListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;

public class CreateMultiplayerRoomActivity extends ButtonsListActivity {

    private ImageButton back;
    private EditText numOfPlayers;
    private CheckBox isPlayingInGroups;
    private int playersNum;
    private LearnedExercises exercises;
    private LearnedExercise currentExercise;
    private Users allUserEmails;
    private Users allUserEmailsWithoutMine;
    private GameFormatter formatter;
    ActivityResultLauncher<Intent> readyUsersListActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == GeneralExerciseConstants.readyUsersListActivityResultCode) {
                Intent intent = result.getData();
                if (intent != null) {
                    allUserEmailsWithoutMine = (Users) intent.getSerializableExtra(GeneralExerciseConstants.readyUsersAllUsersMultiplayerGameExtrasName);
                    User[] usersArrayIncludingMe = new User[allUserEmailsWithoutMine.getUsers().length + 1];
                    System.arraycopy(allUserEmailsWithoutMine.getUsers(), 0, usersArrayIncludingMe, 0, allUserEmailsWithoutMine.getUsers().length);
                    usersArrayIncludingMe[usersArrayIncludingMe.length-1] = new User(formatter.getMyFormattedIdentifier());
                    allUserEmails = new Users(usersArrayIncludingMe);
                    Bundle bundle = new Bundle();
                    currentExercise = exercises.getRandomExercise();
                    CreationGameDataMethod method = new CreationGameDataMethod(currentExercise, allUserEmails, allUserEmailsWithoutMine, formatter.getMyFormattedIdentifier(), formatter);
                    bundle.putSerializable(GeneralExerciseConstants.gameDataMethodExtrasName, method);
                    ActivityOpeningHelper.openActivityForResult(CreateMultiplayerRoomActivity.this, ShowGameDataBeforeNextGame.class, bundle, showGameDataBeforeNextGameActivityLauncher);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> showGameDataBeforeNextGameActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == GeneralExerciseConstants.readyUsersListActivityResultCode) {
                Intent intent = result.getData();
                if (intent != null) {
                    ExerciseMainOperations.openMultiplayerExerciseActivity(CreateMultiplayerRoomActivity.this, exercises, formatter.getMyFormattedIdentifier(), exercises.getExerciseSign(), allUserEmails, allUserEmailsWithoutMine, true, currentExercise, formatter);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_multiplayer_room);
        back = findViewById(R.id.back2);
        back.setOnClickListener(this);
        numOfPlayers = findViewById(R.id.num_of_players);
        isPlayingInGroups = findViewById(R.id.groups);
    }

    @Override
    public List<CharSequence> generateListItems() {
        try {
            Character[] exerciseSigns = LearnedExerciseManager.getAllSavedSigns(this);
            ArrayList<CharSequence> items = new ArrayList<>(exerciseSigns.length);
            for (Character c : exerciseSigns)
                items.add(c.toString());
            return items;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ListFragment getListFragment() {
        ListFragment fragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment;
    }

    @Override
    public View.OnClickListener getOnClickListener(String buttonText) {
        return v -> {
            try {
                playersNum = Integer.parseInt(numOfPlayers.getText().toString());
                if (playersNum <= 0)
                    throw new Exception();
                if (playersNum >= 1000000) {
                    UserMessages.showToastMessage("You can not create a room with more than 1,000,000 people.", this);
                    return;
                }
                char sign = buttonText.charAt(0);
                exercises = LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(sign, CreateMultiplayerRoomActivity.this);
                Bundle bundle = new Bundle();
                int playersNumWithMe = playersNum+1;
                if (isPlayingInGroups.isChecked())
                    formatter = new GroupsFormatter(1, UserManager.getCurrentUserObject(CreateMultiplayerRoomActivity.this).getUserEmail());
                else
                    formatter = new AllVSAllFormatter(UserManager.getCurrentUserObject(CreateMultiplayerRoomActivity.this).getUserEmail());
                CreationReadyMethod method = new CreationReadyMethod(playersNumWithMe, exercises.getExerciseSign(), formatter.getMyFormattedIdentifier(), formatter);
                bundle.putSerializable(GeneralExerciseConstants.readyUsersMethodExtrasName, method);
                ActivityOpeningHelper.openActivityForResult(CreateMultiplayerRoomActivity.this, ReadyUsersListActivity.class, bundle, readyUsersListActivityLauncher);
            } catch (Exception e) {
                e.printStackTrace();
                UserMessages.showToastMessage(UserMessagesConstants.thereWasAnError, CreateMultiplayerRoomActivity.this);
            }
        };
    }

    @Override
    public View.OnLongClickListener getOnLongClickListener(String buttonText) {
        return v -> false;
    }
}