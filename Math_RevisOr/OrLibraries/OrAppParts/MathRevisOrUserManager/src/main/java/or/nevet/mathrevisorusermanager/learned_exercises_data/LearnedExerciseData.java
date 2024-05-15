package or.nevet.mathrevisorusermanager.learned_exercises_data;

import androidx.annotation.Keep;

import java.io.Serializable;
//The @Keep annotation ensures that the names of the functions/variables in the release keystore will not change due to obfuscating. That way, if you have a debug keystore, and create an account, and then log out and connect from a release keystore, it will know how to create the objects.
@Keep
public class LearnedExerciseData implements Serializable {

    @Keep
    public final int leftNumber;
    @Keep
    public final int rightNumber;
    @Keep
    public final int result;
    @Keep
    public int numOfLearned;

    @Keep
    public LearnedExerciseData(int leftNumber, int rightNumber, int numOfLearned, int result) {
        this.leftNumber = leftNumber;
        this.rightNumber = rightNumber;
        this.numOfLearned = numOfLearned;
        this.result = result;
    }

    //Must have this for fireStore to convert data snapshot to User.
    @Keep
    public LearnedExerciseData() {
        leftNumber = 0;
        rightNumber = 0;
        result = 0;
        numOfLearned = 0;
    }

}
