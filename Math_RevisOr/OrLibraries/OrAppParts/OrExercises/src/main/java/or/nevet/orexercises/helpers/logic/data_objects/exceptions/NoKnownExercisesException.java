package or.nevet.orexercises.helpers.logic.data_objects.exceptions;

import androidx.annotation.Nullable;

public class NoKnownExercisesException extends Exception {
    @Nullable
    @Override
    public String getMessage() {
        return "There are no known exercises";
    }
}
