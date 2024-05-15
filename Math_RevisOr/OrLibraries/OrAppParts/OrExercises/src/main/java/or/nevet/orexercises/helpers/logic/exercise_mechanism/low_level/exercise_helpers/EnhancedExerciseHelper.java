package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers;

import android.content.Context;

import or.nevet.orexercises.helpers.logic.data_objects.exceptions.NoKnownExercisesException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.unfocusable.OrOneLineAutoSizeUnfocusableCursorEditText;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.tts.OrTTS;
import or.nevet.orgeneralhelpers.tts.TTSListener;

//This class is thread safe. You can use it with as many threads as you want simultaneously with no risk. You can call its methods from any thread you want, but calling from main thread is more efficient when calling isAnswerRight.
public class EnhancedExerciseHelper extends ExerciseHelper {

    String answerText = "";

    public EnhancedExerciseHelper(LearnedExercises exercises, Context context) {
        super(exercises, context);
    }

    public synchronized boolean isTheAnswerRight(OrOneLineAutoSizeUnfocusableCursorEditText answerContainer) {
        BackgroundRunningHelper.runCodeOnUiThreadSync(() -> {
            answerText = answerContainer.getString().trim();
        });
        int answer;
        try {
            answer = Integer.parseInt(answerText);
        } catch (Exception ignored) {
            return false;
        }
        return answer == getCorrectAnswer();
    }

    public void generateKnownExercise() {
        int pp = generateRandomNumber(4);
        //25% chance
        if (pp == 0) {
            try {
                LearnedExercise exercise = getExercises().getRandomKnownExercise();
                changeExercise(exercise);
            } catch (NoKnownExercisesException e) {
                generateRandomExercise();
            }
        } else
            generateRandomExercise();
    }

    public void generateRandomExercise() {
        LearnedExercise exercise = getExercises().getRandomExercise();
        changeExercise(exercise);
    }

    public void readExercise() {
        OrTTS.createTTS(getContext(), succeeded -> OrTTS.getTTSHelper().readText(getFirstNumber() + " " + getExercises().getExerciseSign() + " " + getSecondNumber() + " equals " + getCorrectAnswer()));
    }
}
