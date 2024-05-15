package or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

import or.nevet.orexercises.helpers.logic.interfaces.Learnable;

public class LearnedExercise implements Learnable, Parcelable, Serializable {
    private final int leftNumber, rightNumber;
    private final int result;
    private int numOfLearned;

    public LearnedExercise(int leftNumber, int rightNumber, int numOfLearned, int result) {
        this.leftNumber = leftNumber;
        this.rightNumber = rightNumber;
        this.numOfLearned = numOfLearned;
        this.result = result;
    }

    protected LearnedExercise(Parcel in) {
        leftNumber = in.readInt();
        rightNumber = in.readInt();
        result = in.readInt();
        numOfLearned = in.readInt();
    }

    public static final Creator<LearnedExercise> CREATOR = new Creator<LearnedExercise>() {
        @Override
        public LearnedExercise createFromParcel(Parcel in) {
            return new LearnedExercise(in);
        }

        @Override
        public LearnedExercise[] newArray(int size) {
            return new LearnedExercise[size];
        }
    };

    public int getLeftNumber() {
        return leftNumber;
    }

    public void exerciseWasSucceeded() {
        numOfLearned++;
    }

    public int getNumOfLearned() {
        return numOfLearned;
    }

    public int getRightNumber() {
        return rightNumber;
    }

    @Override
    public boolean isLearned() {
        return getNumOfLearned() >= 15;
    }

    public int getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearnedExercise that = (LearnedExercise) o;
        return leftNumber == that.leftNumber && rightNumber == that.rightNumber && result == that.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftNumber, rightNumber, result, numOfLearned);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(leftNumber);
        dest.writeInt(rightNumber);
        dest.writeInt(result);
        dest.writeInt(numOfLearned);
    }
}
