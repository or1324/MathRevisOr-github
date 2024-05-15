package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager;

import android.content.Context;

import or.nevet.orgeneralhelpers.constants.FilesConstants;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;

class RevisingExerciseFileHelper extends ExerciseFileHelper {

    RevisingExerciseFileHelper(Context context) {
        super(context);
    }

    protected String getFileNameFromExerciseSign(char exerciseSign) {
        return FilesConstants.revisingExerciseFilePrefix+exerciseSign+ FilesConstants.textFileExtension;
    }
}
