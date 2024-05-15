package or.nevet.mathrevisorusermanager.learned_exercises_data;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//The @Keep annotation ensures that the names of the functions/variables in the release keystore will not change due to obfuscating. That way, if you have a debug keystore, and create an account, and then log out and connect from a release keystore, it will know how to create the objects.
@Keep
public class LearnedExercisesData implements Serializable {
    @Keep
    public HashMap<String, LearnedExerciseData> learnedExercises;
    @Keep
    public ArrayList<Integer> leftNumbersOrderedByTheirIndexes;
    @Keep
    public ArrayList<Integer> rightNumbersOrderedByTheirIndexes;
    @Keep
    public String exerciseSign;

    //Must have this for fireStore to convert data snapshot to User.
    @Keep
    public LearnedExercisesData() {
        learnedExercises = null;
        leftNumbersOrderedByTheirIndexes = null;
        rightNumbersOrderedByTheirIndexes = null;
        exerciseSign = null;
    }
    @Keep
    public LearnedExercisesData(HashMap<String, LearnedExerciseData> learnedExercises, ArrayList<Integer> leftNumbersOrderedByTheirIndexes, ArrayList<Integer> rightNumbersOrderedByTheirIndexes, char exerciseSign) {
        this.learnedExercises = learnedExercises;
        this.exerciseSign = String.valueOf(exerciseSign);
        this.leftNumbersOrderedByTheirIndexes = leftNumbersOrderedByTheirIndexes;
        this.rightNumbersOrderedByTheirIndexes = rightNumbersOrderedByTheirIndexes;
    }

}
