package or.nevet.math_revisor.main;

import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.widget.ImageButton;

import java.text.DecimalFormat;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedDivExercises;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedMulExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class StatisticsActivity extends MusicSubActivity {

    public ImageButton openTableMul;
    public ImageButton openTableDiv;
    public ImageButton back;
    public AppCompatTextView percentsMul;
    public AppCompatTextView percentsDiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        AppGraphics.initStatistics(this);
        showProgress();
    }

    private void showProgress() {
        LearnedMulExercises mulExercises = LearnedExerciseManager.getLearnedMulExercises(this);
        LearnedDivExercises divExercises = LearnedExerciseManager.getLearnedDivExercises(this);
        String percentsLearnedMul;
        int numOfLearnedMul = mulExercises.getTotalNumOfLearnedWell();
        //int numOfSuccessesMul = mulExercises.getTotalNumOfSuccesses();
        String percentsLearnedDiv;
        int numOfLearnedDiv = divExercises.getTotalNumOfLearnedWell();
        //int numOfSuccessesDiv = divExercises.getTotalNumOfSuccesses();
        percentsLearnedMul = getLearnedPercents(numOfLearnedMul, mulExercises);
        percentsLearnedDiv = getLearnedPercents(numOfLearnedDiv, divExercises);
        percentsMul.setText(percentsLearnedMul+"%");
        percentsDiv.setText(percentsLearnedDiv+"%");
        //progress.setText("Num Of Successes Mul: " + numOfSuccessesMul + "\nNum Of Learned Mul: " + numOfLearnedMul + "\nPercents Learned Mul: " + percentsLearnedMul + "\n\nNum Of Successes Div: " + numOfSuccessesDiv + "\nNum Of Learned Div: " + numOfLearnedDiv + "\nPercents Learned Div: " + percentsLearnedDiv);
    }

    private String getLearnedPercents(int numOfLearned, LearnedExercises exercises) {
        double learnedPercents = 100d*(((double)numOfLearned)/ (double) exercises.getTotalNumOfExercises());
        return new DecimalFormat("###,###.##").format(learnedPercents);
    }
}