package or.nevet.orexercises.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.LearnedExercisesIndexesHelper;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orexercises.helpers.visual.CustomExerciseTable;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.constants.TablesConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.MoveCutViewContainOr;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class ExerciseCreationTableActivity extends MusicSubActivity {

    private CustomExerciseTable table = null;
    public AppCompatImageButton createExercise;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_creation_table);
        boolean isEditing = getIntent().getExtras().getBoolean(GeneralExerciseConstants.isExerciseEditingExtrasName);
        char sign = getIntent().getExtras().getChar(GeneralExerciseConstants.exerciseSignExtrasName);
        int numOfRows = getIntent().getExtras().getInt(GeneralExerciseConstants.numOfRowsExtrasName);
        int numOfColumns = getIntent().getExtras().getInt(GeneralExerciseConstants.numOfColumnsExtrasName);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        createExercise = findViewById(R.id.create_exercise);
        createExercise.setOnClickListener(v1 -> {
            saveExercise(numOfRows, numOfColumns, sign);
        });
        try {
            if (isEditing) {
                LearnedExercises exercises = LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(sign, ExerciseCreationTableActivity.this);
                Integer[] leftNumbers = new Integer[exercises.getAmountOfLeftNumbers()];
                exercises.getLeftNumbersOrderedArrayList().toArray(leftNumbers);
                Integer[] rightNumbers = new Integer[exercises.getAmountOfRightNumbers()];
                exercises.getRightNumbersOrderedArrayList().toArray(rightNumbers);
                switchToTableForEditing(numOfRows, numOfColumns, sign, leftNumbers, rightNumbers, exercises.getAnswersMatrixWithNullWhereEmpty());
            } else {
                switchToTableForCreating(numOfRows, numOfColumns, sign);
            }
        } catch (OutOfMemoryError e) {
            UserMessages.showEmptyDialogMessage("Your phone does not have enough memory to handle this.", this, () -> finish());
        }
    }

    public void switchToTableForEditing(int numOfExercisesInRow, int numOfExercisesInColumn, char sign, Integer[] initialLeftValues, Integer[] initialRightValues, Integer[][] initialAnswerValues) {
        createExercise.setVisibility(View.VISIBLE);
        table = new CustomExerciseTable(this, numOfExercisesInRow, numOfExercisesInColumn, TablesConstants.customExerciseTableCellSizeInPixels, sign, initialLeftValues, initialRightValues, initialAnswerValues);
        showTableInsideMoveCutViewContainOr();
    }

    public void switchToTableForCreating(int numOfExercisesInRow, int numOfExercisesInColumn, char sign) {
        createExercise.setVisibility(View.VISIBLE);
        table = new CustomExerciseTable(this, numOfExercisesInRow, numOfExercisesInColumn, TablesConstants.customExerciseTableCellSizeInPixels, sign, null, null, null);
        showTableInsideMoveCutViewContainOr();
    }

    private void showTableInsideMoveCutViewContainOr() {
        MoveCutViewContainOr moveCutViewContainOr = findViewById(R.id.move_cut_container);
        moveCutViewContainOr.initView(table);
        moveCutViewContainOr.askForShow();
    }

    public void saveExercise(int numOfLeft, int numOfRight, char sign) {
        HashSet<Integer> numOfLeftsSet = new HashSet<>();
        HashSet<Integer> numOfRightsSet = new HashSet<>();
        if (checkForErrors(numOfLeft, numOfRight))
            return;
        for (int i = 0; i < numOfLeft; i++)
            numOfLeftsSet.add(table.getLeftNumberAtRow(i));
        for (int i = 0; i < numOfRight; i++)
            numOfRightsSet.add(table.getRightNumberAtColumn(i));
        if (numOfLeftsSet.size() != numOfLeft) {
            UserMessages.showToastMessage(UserMessagesConstants.canNotHaveMultipleEqualLeftNumbers, this);
            return;
        }
        if (numOfRightsSet.size() != numOfRight) {
            UserMessages.showToastMessage(UserMessagesConstants.canNotHaveMultipleEqualRightNumbers, this);
            return;
        }
        HashMap<String, LearnedExercise> learnedExercises = new HashMap<>();
        ArrayList<Integer> leftNumbers = new ArrayList<>(numOfLeft);
        ArrayList<Integer> rightNumbers = new ArrayList<>(numOfRight);
        for (int i = 0; i < numOfLeft; i++)
            leftNumbers.add(table.getLeftNumberAtRow(i));
        for (int i = 0; i < numOfRight; i++)
            rightNumbers.add(table.getRightNumberAtColumn(i));
        boolean isThereAtLeastOneAnswer = false;
        //init learned exercises
        for (int indexInFirstArrayOfLearnedExercises = 0; indexInFirstArrayOfLearnedExercises < numOfLeft; indexInFirstArrayOfLearnedExercises++) {
            for (int indexInSecondArrayOfLearnedExercises = 0; indexInSecondArrayOfLearnedExercises < numOfRight; indexInSecondArrayOfLearnedExercises++) {
                Integer currentResult = table.getAnswerAtCell(indexInFirstArrayOfLearnedExercises, indexInSecondArrayOfLearnedExercises);
                if (currentResult != null) {
                    isThereAtLeastOneAnswer = true;
                    learnedExercises.put(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(indexInFirstArrayOfLearnedExercises, indexInSecondArrayOfLearnedExercises), new LearnedExercise(table.getLeftNumberAtRow(indexInFirstArrayOfLearnedExercises), table.getRightNumberAtColumn(indexInSecondArrayOfLearnedExercises), 0, currentResult));
                }
            }
        }
        if (!isThereAtLeastOneAnswer) {
            UserMessages.showToastMessage(UserMessagesConstants.youMustFillAtLeastOneAnswer, this);
            return;
        }
        LearnedExercises exercises = new LearnedExercises(learnedExercises, leftNumbers, rightNumbers, sign);
        LearnedExerciseManager.saveSpecificLearnedExercises(this, exercises);
        UserMessages.showToastMessage(UserMessagesConstants.exercisesAddedSuccessfully, this);
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            try {
                UserManager.uploadUser();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        Intent result = new Intent();
        result.putExtra(GeneralExerciseConstants.manageCustomExercisesNewExerciseSignExtra, sign);
        setResult(GeneralExerciseConstants.customExerciseCreationResultCode, result);
        finish();
    }

    private boolean checkForErrors(int numOfLeft, int numOfRight) {
        for (int i = 0; i < numOfLeft; i++) {
            try {
                if (table.getLeftNumberAtRow(i) == null) {
                    UserMessages.showToastMessage(UserMessagesConstants.allLeftAndRightNumbersMustBeFilled, this);
                    return true;
                }
            } catch (RuntimeException e) {
                UserMessages.showToastMessage(UserMessagesConstants.theNumberIsTooBig, this);
                return true;
            }
        }
        for (int i = 0; i < numOfRight; i++) {
            try {
                if (table.getRightNumberAtColumn(i) == null) {
                    UserMessages.showToastMessage(UserMessagesConstants.allLeftAndRightNumbersMustBeFilled, this);
                    return true;
                }
            } catch (RuntimeException e) {
                UserMessages.showToastMessage(UserMessagesConstants.theNumberIsTooBig, this);
                return true;
            }
        }
        for (int indexInFirstArrayOfLearnedExercises = 0; indexInFirstArrayOfLearnedExercises < numOfLeft; indexInFirstArrayOfLearnedExercises++) {
            for (int indexInSecondArrayOfLearnedExercises = 0; indexInSecondArrayOfLearnedExercises < numOfRight; indexInSecondArrayOfLearnedExercises++) {
                try {
                    table.getAnswerAtCell(indexInFirstArrayOfLearnedExercises, indexInSecondArrayOfLearnedExercises);
                } catch (RuntimeException e) {
                    UserMessages.showToastMessage(UserMessagesConstants.theNumberIsTooBig, this);
                    return true;
                }
            }
        }
        return false;
    }

}