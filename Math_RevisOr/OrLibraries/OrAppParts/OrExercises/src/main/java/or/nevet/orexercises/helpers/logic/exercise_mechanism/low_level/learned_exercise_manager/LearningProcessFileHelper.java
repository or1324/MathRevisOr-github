package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import or.nevet.orgeneralhelpers.constants.FilesConstants;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.ExternalStorage;

class LearningProcessFileHelper {
    static void saveLearningProcess(Context context, char exerciseSign, int learningProcess) {
        String fileName = getFileNameFromExerciseSign(exerciseSign);
        ExternalStorage.saveObject(context, fileName, learningProcess);
    }

    static int restoreLearningProcess(Context context, char exerciseSign) throws Exception {
        String fileName = getFileNameFromExerciseSign(exerciseSign);
        int learningProcess = (int) ExternalStorage.restoreObject(context, fileName);
        return learningProcess;
    }

    private static String getFileNameFromExerciseSign(char exerciseSign) {
        return FilesConstants.learningProcessFilePrefix+exerciseSign+ FilesConstants.textFileExtension;
    }
}
