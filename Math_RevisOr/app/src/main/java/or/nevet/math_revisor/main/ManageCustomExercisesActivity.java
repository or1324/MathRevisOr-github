package or.nevet.math_revisor.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.main.ExerciseMainOperations;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.activity_types.ButtonsListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;

public class ManageCustomExercisesActivity extends ButtonsListActivity {

    public AppCompatImageButton back;
    private AppCompatImageButton addCustomExercise;
    private ListFragment listFragment;

    private ActivityResultLauncher<Intent> customExerciseCreationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == GeneralExerciseConstants.customExerciseCreationResultCode) {
                Intent intent = result.getData();
                if (intent != null) {
                    char newExerciseSign = intent.getCharExtra(GeneralExerciseConstants.manageCustomExercisesNewExerciseSignExtra, GeneralExerciseConstants.defaultExerciseSign);
                    listFragment.addItemToStartOfList(String.valueOf(newExerciseSign), ManageCustomExercisesActivity.this);
                }
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);
        AppGraphics.initManageCustomExercises(this);
        addCustomExercise = findViewById(R.id.add_custom_exercise);
        addCustomExercise.setOnClickListener(v -> ActivityOpeningHelper.openActivityForResult(this, CustomExerciseCreationActivity.class, null, customExerciseCreationLauncher));
    }

    @Override
    public List<CharSequence> generateListItems() {
        try {
            Character[] exerciseSigns = LearnedExerciseManager.getCustomExercisesSavedSigns(ManageCustomExercisesActivity.this);
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
    public View.OnClickListener getOnClickListener(String buttonText) {
        return v -> {
            char sign = buttonText.charAt(0);
            LearnedExercises exercises = LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(sign, ManageCustomExercisesActivity.this);
            ExerciseMainOperations.openActivityAndSendExercises(ManageCustomExercisesActivity.this, ExerciseMainOperations.ExerciseActivityOption.PracticeExerciseActivityWithoutScore, exercises);
        };
    }

    @Override
    public View.OnLongClickListener getOnLongClickListener(String buttonText) {
        return v -> {
            final char sign = buttonText.charAt(0);
            LinkedHashMap<String, Runnable> buttons = new LinkedHashMap<>();
            buttons.put("Stats", () -> {
                LearnedExercises exercises = LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(sign, ManageCustomExercisesActivity.this);
                ExerciseMainOperations.openActivityAndSendExercises(ManageCustomExercisesActivity.this, ExerciseMainOperations.ExerciseActivityOption.ExerciseStatsTableActivity, exercises);
            });
            buttons.put("Edit", () -> {
                LearnedExercises exercises = LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(sign, ManageCustomExercisesActivity.this);
                ExerciseMainOperations.openExerciseCreationTable(ManageCustomExercisesActivity.this, sign, exercises.getAmountOfLeftNumbers(), exercises.getAmountOfRightNumbers(),true, false);
            });
            buttons.put("Remove", () -> {
                UserMessages.showButtonDialogQuestion(UserMessagesConstants.areYouSureThatYouWantToRemoveThisExercise, ManageCustomExercisesActivity.this, (Runnable) () -> {
                    removeExerciseAndSaveInCloudAndRefreshList(sign);
                });
            });
            UserMessages.showButtonsDialogOptions(UserMessagesConstants.chooseAnOption, ManageCustomExercisesActivity.this, buttons);
            return true;
        };
    }

    private void removeExerciseAndSaveInCloudAndRefreshList(char sign) {
        LearnedExerciseManager.removeSpecificLearnedExercisesFromStorage(ManageCustomExercisesActivity.this, sign);
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            try {
                UserManager.uploadUser();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        listFragment.removeItemFromList(String.valueOf(sign), this);
    }

    @Override
    public ListFragment getListFragment() {
        listFragment =  (ListFragment) getSupportFragmentManager().findFragmentById(or.nevet.orexercises.R.id.fragment_container);
        return listFragment;
    }
}