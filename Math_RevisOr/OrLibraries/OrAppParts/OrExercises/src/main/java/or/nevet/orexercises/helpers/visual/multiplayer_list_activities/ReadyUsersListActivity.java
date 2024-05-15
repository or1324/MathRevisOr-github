package or.nevet.orexercises.helpers.visual.multiplayer_list_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.CreationReadyMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.JoiningReadyMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.ReadyUsersMethod;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;

public class ReadyUsersListActivity extends ListActivity {

    private ReadyUsersMethod method;
    private ImageButton back;
    private AppCompatImageView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_users_list);
        back = findViewById(R.id.back2);
        title = findViewById(R.id.ready_title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(GeneralExerciseConstants.readyUsersListActivityResultCode, null);
                finish();
            }
        });
        method = (ReadyUsersMethod) getIntent().getExtras().getSerializable(GeneralExerciseConstants.readyUsersMethodExtrasName);
    }

    @Override
    public List<CharSequence> generateListItems() {
        Serializable[] returnedValue = new Serializable[1];
        boolean[] finished = new boolean[1];
        Users[] usersArray = new Users[1];
        if (method.getClass() == CreationReadyMethod.class)
            setCreationStartTitle();
        returnedValue[0] = method.runMethod(new OrReadyUsersListener() {
            @Override
            public void onUserReady(User user) {
                addItemToList(method.formatReadyUser(user));
            }

            @Override
            public void onAllUsersReady(Users users) {
                usersArray[0] = users;
                if (finished[0])
                    closeActivityBecauseAllUsersAreReady(returnedValue[0], users);
            }
        });
        if (returnedValue[0] == null) {
            closeActivityForJoiningUserBecauseRoomDoesNotExist();
            return new LinkedList<>();
        }
        finished[0] = true;
        if (usersArray[0] != null)
            closeActivityBecauseAllUsersAreReady(returnedValue[0], usersArray[0]);
        if (method.getClass() != JoiningReadyMethod.class && method.getClass() != CreationReadyMethod.class) {
            //it is not creation and not joining, So this is game.
            setGameTitle();
        }
        if (method.getClass() == CreationReadyMethod.class || method.getClass() == JoiningReadyMethod.class)
            setMatchingEndTitle();
        if (getListFragment() != null)
            getListFragment().restartProgressBar();
        return new LinkedList<>();
    }

    private void setGameTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(ReadyUsersListActivity.this, R.drawable.ready_users_title_on_game)));
    }

    private void setCreationStartTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(ReadyUsersListActivity.this, R.drawable.ready_users_title_on_creation_start)));
    }

    private void setMatchingEndTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(ReadyUsersListActivity.this, R.drawable.ready_users_title_on_matching_end)));
    }

    private void closeActivityBecauseAllUsersAreReady(Serializable returnedValue, Users users) {
        Intent intent = new Intent();
        intent.putExtra(GeneralExerciseConstants.readyUsersReturnedValueMultiplayerGameExtrasName, returnedValue);
        intent.putExtra(GeneralExerciseConstants.readyUsersAllUsersMultiplayerGameExtrasName, users);
        setResult(GeneralExerciseConstants.readyUsersListActivityResultCode, intent);
        finish();
    }

    private void closeActivityForJoiningUserBecauseRoomDoesNotExist() {
        Intent intent = new Intent();
        intent.putExtra(GeneralExerciseConstants.readyUsersReturnedValueMultiplayerGameExtrasName, (Serializable) null);
        setResult(GeneralExerciseConstants.readyUsersListActivityResultCode, intent);
        finish();
    }

    @Override
    public ListFragment getListFragment() {
        ListFragment fragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        setResult(GeneralExerciseConstants.readyUsersListActivityResultCode, null);
        super.onBackPressed();
    }
}
