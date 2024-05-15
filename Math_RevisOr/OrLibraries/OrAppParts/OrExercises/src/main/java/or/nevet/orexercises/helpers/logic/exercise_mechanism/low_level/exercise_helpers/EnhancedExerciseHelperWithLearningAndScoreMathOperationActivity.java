package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers;

import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearning;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.score_helpers.LeaderboardScoreHelper;

//This class is thread safe. You can use it with as many threads as you want simultaneously with almost no risk. You can call its methods from any thread you want, but calling from main thread is more efficient when calling isAnswerRight.
public class EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity extends EnhancedExerciseHelperWithLearningMathOperationActivity {

    LeaderboardScoreHelper scoreHelper;

    public EnhancedExerciseHelperWithLearningAndScoreMathOperationActivity(MathOperationWithLearning operation) {
        super(operation);
        scoreHelper = new LeaderboardScoreHelper(operation.getExerciseActivity().getScoreView(), operation.getExerciseActivity());
    }

    public void exerciseWasSucceeded() {
        super.exerciseWasSucceeded();
        scoreHelper.exerciseWasSucceeded();
    }

    public void wrong() {
        scoreHelper.wrong();
    }
}
