package or.nevet.math_revisor.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.orexercises.main.ExerciseMainOperations;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.focusable.OrOneLineAutoSizeFocusableEditText;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;


public class CustomExerciseCreationActivity extends MusicSubActivity {

    public AppCompatImageButton back;
    public AppCompatImageButton writeExerciseAnswers;
    public AppCompatTextView instructions;
    public OrOneLineAutoSizeFocusableEditText numOfLinesLeft;
    public OrOneLineAutoSizeFocusableEditText numOfLinesRight;
    public OrOneLineAutoSizeFocusableEditText signEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_exercise_creation);
        AppGraphics.initCustomExerciseCreation(this);
    }

    public void openExerciseCreationTable(char sign, int numOfLeft, int numOfRight) {
        ExerciseMainOperations.openExerciseCreationTable(this, sign, numOfLeft, numOfRight, false, true);
    }
}