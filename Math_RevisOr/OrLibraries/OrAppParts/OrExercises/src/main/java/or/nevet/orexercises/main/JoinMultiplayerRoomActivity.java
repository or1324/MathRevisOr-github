package or.nevet.orexercises.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.AllVSAllFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GroupsFormatter;
import or.nevet.orgeneralhelpers.CommonArrayAlgorithms;
import or.nevet.orgeneralhelpers.Email;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.JoiningGameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.JoiningReadyMethod;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ReadyUsersListActivity;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ShowGameDataBeforeNextGame;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;

public class JoinMultiplayerRoomActivity extends MusicSubActivity {

    private ImageButton back;
    private Button join;
    private EditText ownerEmailTextBox;
    private RadioGroup orCoolSwitch;
    private Spinner groupsDropDown;
    private String ownerFormattedEmail;
    private Users allUsersWithMe;
    private Users allUsersWithoutMe;
    private char exerciseSign;
    private GameFormatter formatter;
    ActivityResultLauncher<Intent> readyUsersListActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == GeneralExerciseConstants.readyUsersListActivityResultCode) {
                Intent intent = result.getData();
                if (intent != null) {
                    allUsersWithoutMe = (Users) intent.getSerializableExtra(GeneralExerciseConstants.readyUsersAllUsersMultiplayerGameExtrasName);
                    Character character = (Character) intent.getSerializableExtra(GeneralExerciseConstants.readyUsersReturnedValueMultiplayerGameExtrasName);
                    if (character == null) {
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.roomDoesNotExist, JoinMultiplayerRoomActivity.this);
                        return;
                    }
                    if (allUsersWithoutMe == null) {
                        //The owner has started a new game with other users. We know that because the ready users collection was deleted, which caused the joinRoom method to make this string array null.
                        UserMessages.showEmptyDialogMessage(UserMessagesConstants.roomOwnerStartedAnotherGame, JoinMultiplayerRoomActivity.this);
                        return;
                    }
                    User[] usersArrayIncludingMe = new User[allUsersWithoutMe.getUsers().length + 1];
                    System.arraycopy(allUsersWithoutMe.getUsers(), 0, usersArrayIncludingMe, 0, allUsersWithoutMe.getUsers().length);
                    usersArrayIncludingMe[usersArrayIncludingMe.length-1] = new User(formatter.getMyFormattedIdentifier());
                    allUsersWithMe = new Users(usersArrayIncludingMe);
                    exerciseSign = character;
                    Bundle bundle = new Bundle();
                    JoiningGameDataMethod method = new JoiningGameDataMethod(allUsersWithoutMe, formatter.getMyFormattedIdentifier(), ownerFormattedEmail, allUsersWithMe, formatter);
                    bundle.putSerializable(GeneralExerciseConstants.gameDataMethodExtrasName, method);
                    ActivityOpeningHelper.openActivityForResult(JoinMultiplayerRoomActivity.this, ShowGameDataBeforeNextGame.class, bundle, showGameDataBeforeNextGameActivityLauncher);
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
                    LearnedExerciseData data = (LearnedExerciseData) intent.getExtras().getSerializable(GeneralExerciseConstants.readyUsersReturnedValueMultiplayerGameExtrasName);
                    LearnedExercise currentExercise = new LearnedExercise(data.leftNumber, data.rightNumber, 0, data.result);
                    ExerciseMainOperations.openMultiplayerExerciseActivity(JoinMultiplayerRoomActivity.this, null, ownerFormattedEmail, exerciseSign, allUsersWithMe, allUsersWithoutMe, false, currentExercise, formatter);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_multiplayer_room);
        back = findViewById(R.id.back2);
        back.setOnClickListener(this);
        join = findViewById(R.id.join);
        ownerEmailTextBox = findViewById(R.id.owner_email);
        orCoolSwitch = findViewById(R.id.or_cool_switch);
        groupsDropDown = findViewById(R.id.group_list);
        orCoolSwitch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.all_vs_all) {
                groupsDropDown.setVisibility(View.GONE);
            } else if (checkedId == R.id.groups) {
                groupsDropDown.setVisibility(View.VISIBLE);
            }
        });
        join.setOnClickListener(v -> {
            ownerFormattedEmail = new Email(ownerEmailTextBox.getText().toString()).toString();
            if (!CommonArrayAlgorithms.charArrayContainsAllStringCharacters(GeneralConstants.emailValidCharacters, ownerFormattedEmail)) {
                UserMessages.showEmptyDialogMessage(UserMessagesConstants.emailNotValid, JoinMultiplayerRoomActivity.this);
                return;
            }
            GameFormatter ownerEmailFormatter;
            if (orCoolSwitch.getCheckedRadioButtonId() == R.id.groups) {
                formatter = new GroupsFormatter(getSelectedGroupNumber(), UserManager.getCurrentUserObject(JoinMultiplayerRoomActivity.this).getUserEmail());
                ownerEmailFormatter = new GroupsFormatter(1, UserManager.getCurrentUserObject(JoinMultiplayerRoomActivity.this).getUserEmail());
            }
            else {
                formatter = new AllVSAllFormatter(UserManager.getCurrentUserObject(JoinMultiplayerRoomActivity.this).getUserEmail());
                ownerEmailFormatter = new AllVSAllFormatter(UserManager.getCurrentUserObject(JoinMultiplayerRoomActivity.this).getUserEmail());
            }
            ownerFormattedEmail = ownerEmailFormatter.formatIdentifier(ownerFormattedEmail);
            Bundle bundle = new Bundle();
            JoiningReadyMethod method = new JoiningReadyMethod(ownerFormattedEmail, formatter.getMyFormattedIdentifier(), formatter);
            bundle.putSerializable(GeneralExerciseConstants.readyUsersMethodExtrasName, method);
            ActivityOpeningHelper.openActivityForResult(JoinMultiplayerRoomActivity.this, ReadyUsersListActivity.class, bundle, readyUsersListActivityLauncher);
        });
    }

    private int getSelectedGroupNumber() {
        return groupsDropDown.getSelectedItemPosition()+1;
    }
}