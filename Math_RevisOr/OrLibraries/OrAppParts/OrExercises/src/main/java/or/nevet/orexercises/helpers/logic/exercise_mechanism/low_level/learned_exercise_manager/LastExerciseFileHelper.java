package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import or.nevet.orgeneralhelpers.constants.FilesConstants;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;

class LastExerciseFileHelper extends ExerciseFileHelper {

    LastExerciseFileHelper(Context context) {
        super(context);
    }

    protected String getFileNameFromExerciseSign(char exerciseSign) {
        return FilesConstants.lastExerciseFilePrefix+exerciseSign+ FilesConstants.textFileExtension;
    }
}
