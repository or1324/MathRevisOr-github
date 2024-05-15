package or.nevet.orexercises.main;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearning;
import or.nevet.orgeneralhelpers.tts.OrTTS;

public class PracticeExerciseActivityWithoutScore extends LearningExerciseActivity {
    @Override
    protected void createMathOperation(LearnedExercises exercises) {
        mathOperation = new MathOperationWithLearning(this, OrTTS.getTTSHelper(), exercises);
    }
}
