package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers;

import android.content.Context;

import java.io.Serializable;
import java.util.Random;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;

//This class is thread safe. You can use it with as many threads as you want simultaneously with almost no risk. You can call its methods from any thread you want, but calling from main thread is more efficient.
public abstract class ExerciseHelper {

    private LearnedExercise currentExercise;
    private final LearnedExercises exercises;
    private final Random random;
    private LearnedExercise needsRevisingExercise;
    private final Context context;

    public ExerciseHelper(LearnedExercises exercises, Context context) {
        this.context = context;
        this.exercises = exercises;
        random = new Random();
    }

    public int getCorrectAnswer() {
        return currentExercise.getResult();
    }

    protected int generateRandomNumber(int bound) {
        return random.nextInt(bound);
    }

    public LearnedExercise getCurrentExercise() {
        return currentExercise;
    }

    public int getFirstNumber() {
        return currentExercise.getLeftNumber();
    }

    public int getSecondNumber() {
        return currentExercise.getRightNumber();
    }

    public LearnedExercise getNeedsRevisingExercise() {
        return needsRevisingExercise;
    }

    protected void changeExercise(LearnedExercise newExercise) {
        currentExercise = newExercise;
    }

    protected void changeRevisingExercise(LearnedExercise newRevisingExercise) {
        needsRevisingExercise = newRevisingExercise;
    }

    protected Context getContext() {
        return context;
    }

    public boolean isLearned() {
        return currentExercise.isLearned();
    }

    public LearnedExercises getExercises() {
        return exercises;
    }

}
