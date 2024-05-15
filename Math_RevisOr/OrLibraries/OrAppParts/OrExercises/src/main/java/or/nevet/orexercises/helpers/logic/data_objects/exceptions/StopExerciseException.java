package or.nevet.orexercises.helpers.logic.data_objects.exceptions;

import androidx.annotation.Nullable;

public class StopExerciseException extends Exception {
    @Nullable
    @Override
    public String getMessage() {
        return "The exercise was stopped by the user";
    }
}
