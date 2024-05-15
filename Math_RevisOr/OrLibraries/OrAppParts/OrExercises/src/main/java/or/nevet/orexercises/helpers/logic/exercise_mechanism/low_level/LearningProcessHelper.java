package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level;

import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.MathOperationWithLearning;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;

public class LearningProcessHelper {
    //everything that is related to learningProcess.

    MathOperationWithLearning operation;

    public LearningProcessHelper(MathOperationWithLearning operation) {
        this.operation = operation;
        restoreLearn();
    }

    public void nextLearn() {
        operation.setLearningProcess(operation.getLearningProcess()-1);
        writeLearn();
    }

    private void restoreLearn() {
        try {
            operation.setLearningProcess(LearnedExerciseManager.getSavedLearningProcess(operation.getExerciseActivity(), operation.getExerciseSign()));
        } catch (Exception e) {
            operation.setLearningProcess(0);
        }
    }

    public void writeLearn() {
        LearnedExerciseManager.saveLearningProcess(operation.getExerciseActivity(), operation.getExerciseSign(), operation.getLearningProcess());
    }

}
