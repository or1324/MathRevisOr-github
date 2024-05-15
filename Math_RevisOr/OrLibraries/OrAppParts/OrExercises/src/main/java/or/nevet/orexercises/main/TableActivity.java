package or.nevet.orexercises.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import com.github.chrisbanes.photoview.PhotoView;

import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.visual.AppGraphics;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class TableActivity extends MusicSubActivity {

    public PhotoView photoView;
    public AppCompatImageButton back;
    public TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        LearnedExercises exercises = getIntent().getExtras().getParcelable(GeneralExerciseConstants.exercisesExtrasName);
        AppGraphics.initExerciseStatisticsTable(this, exercises);
    }
}