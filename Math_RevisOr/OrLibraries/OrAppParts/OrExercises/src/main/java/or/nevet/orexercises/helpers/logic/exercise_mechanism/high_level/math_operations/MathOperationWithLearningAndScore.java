package or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers.EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;
import or.nevet.orgeneralhelpers.tts.OrTTS;

public class MathOperationWithLearningAndScore extends MathOperationWithLearning {

    EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity learningAndScoreExerciseHelper;


    public MathOperationWithLearningAndScore(ExerciseActivity activity, OrTTS.TTSHelper tts, LearnedExercises exercises) {
        super(activity, tts, exercises);
        learningAndScoreExerciseHelper = getExerciseHelper();
    }

    @Override
    protected EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity getExerciseHelper() {
        return (EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity) super.getExerciseHelper();
    }

    @Override
    protected EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity createExerciseHelper() {
        return new EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity(this);
    }

    @Override
    protected void wrong() {
        learningAndScoreExerciseHelper.wrong();
        super.wrong();
    }
}
