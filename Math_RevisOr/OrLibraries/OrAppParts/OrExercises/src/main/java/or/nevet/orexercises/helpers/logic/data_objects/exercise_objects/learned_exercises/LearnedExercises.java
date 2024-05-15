package or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import or.nevet.orexercises.helpers.logic.data_objects.exceptions.NoKnownExercisesException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.ExercisesIterator;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.LearnedExercisesIndexesHelper;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseIteration;
import or.nevet.orexercises.helpers.logic.interfaces.TableCellIteration;

public class LearnedExercises implements Parcelable, Serializable {
    protected final HashMap<String, LearnedExercise> learnedExercises;
    protected final ArrayList<Integer> leftNumbersOrderedByTheirIndexes;
    protected final ArrayList<Integer> rightNumbersOrderedByTheirIndexes;
    private final ArrayList<String> existingIndexes;
    private final char exerciseSign;
    private final Random random;

    public LearnedExercises(HashMap<String, LearnedExercise> learnedExercises, ArrayList<Integer> leftNumbersOrderedByTheirIndexes, ArrayList<Integer> rightNumbersOrderedByTheirIndexes, char exerciseSign) {
        this.learnedExercises = learnedExercises;
        this.exerciseSign = exerciseSign;
        this.leftNumbersOrderedByTheirIndexes = leftNumbersOrderedByTheirIndexes;
        this.rightNumbersOrderedByTheirIndexes = rightNumbersOrderedByTheirIndexes;
        random = new Random();
        existingIndexes = getExistingIndexes();
    }

    //O(m*n) n=amount of left numbers, m=amount of right numbers. Done only once on start. The creation of this object is already O(n*m) because the learnedExercises that was brought in the constructor must have been created using O(n*m).
    private ArrayList<String> getExistingIndexes() {
        ArrayList<String> existingIndexes = new ArrayList<>(learnedExercises.size());
        iterateAllCellsByOrder((row, column) -> {
            if (getLearnedExerciseAtIndexes(row, column) != null)
                existingIndexes.add(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(row, column));
        });
        //can never happen since the random brings a number which is smaller than the size of the hashmap so the current position will always be equal to the random before the loop stops.
        return existingIndexes;
    }

    protected LearnedExercises(Parcel in) {
        exerciseSign = (char) in.readInt();
        leftNumbersOrderedByTheirIndexes = in.readArrayList(Integer.class.getClassLoader());
        rightNumbersOrderedByTheirIndexes = in.readArrayList(Integer.class.getClassLoader());
        learnedExercises = new HashMap<>(getAmountOfLeftNumbers()*getAmountOfRightNumbers());
        ExercisesIterator.iterateAllCellsByOrder(getAmountOfLeftNumbers(), getAmountOfRightNumbers(), (row, column) -> {
            int leftNumberIndex = (int) in.readValue(Integer.class.getClassLoader());
            int rightNumberIndex = (int) in.readValue(Integer.class.getClassLoader());
            LearnedExercise exercise = in.readParcelable(LearnedExercise.class.getClassLoader());
            if (exercise != null)
                learnedExercises.put(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(leftNumberIndex, rightNumberIndex), exercise);
        });
        existingIndexes = new ArrayList<>(learnedExercises.size());
        for (int i = 0; i < learnedExercises.size(); i++) {
            existingIndexes.add(in.readString());
        }
        random = new Random();
    }

    public static final Creator<LearnedExercises> CREATOR = new Creator<LearnedExercises>() {
        @Override
        public LearnedExercises createFromParcel(Parcel in) {
            return new LearnedExercises(in);
        }

        @Override
        public LearnedExercises[] newArray(int size) {
            return new LearnedExercises[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(exerciseSign);
        dest.writeList(leftNumbersOrderedByTheirIndexes);
        dest.writeList(rightNumbersOrderedByTheirIndexes);
        ExercisesIterator.iterateAllCellsByOrder(getAmountOfLeftNumbers(), getAmountOfRightNumbers(), (row, column) -> {
            LearnedExercise exercise = learnedExercises.get(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(row, column));
            dest.writeValue(row);
            dest.writeValue(column);
            dest.writeParcelable(exercise, flags);
        });
        for (String indexesString : existingIndexes) {
            dest.writeString(indexesString);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearnedExercises that = (LearnedExercises) o;
        return that.exerciseSign == exerciseSign;
    }

    public char getExerciseSign() {
        return exerciseSign;
    }

    //O(1)
    public int getLeftNumberAt(int row) {
        return leftNumbersOrderedByTheirIndexes.get(row);
    }

    //O(1)
    public int getRightNumberAt(int column) {
        return rightNumbersOrderedByTheirIndexes.get(column);
    }

    //O(1)
    public ArrayList<Integer> getLeftNumbersOrderedArrayList() {
        return leftNumbersOrderedByTheirIndexes;
    }

    //O(1)
    public ArrayList<Integer> getRightNumbersOrderedArrayList() {
        return rightNumbersOrderedByTheirIndexes;
    }

    public Integer[][] getAnswersMatrixWithNullWhereEmpty() {
        Integer[][] answersMatrix = new Integer[getAmountOfLeftNumbers()][getAmountOfRightNumbers()];
        iterateAllCellsByOrder(new TableCellIteration() {
            @Override
            public void onIteration(int row, int column) {
                LearnedExercise exercise = getLearnedExerciseAtIndexes(row, column);
                if (exercise == null)
                    answersMatrix[row][column] = null;
                else
                    answersMatrix[row][column] = exercise.getResult();
            }
        });
        return answersMatrix;
    }

    //O(n*m) n=amount of left numbers, m=amount of right numbers
    public void iterateAllCellsByOrder(TableCellIteration iteration) {
        ExercisesIterator.iterateAllCellsByOrder(getAmountOfLeftNumbers(), getAmountOfRightNumbers(), iteration);
    }

    //less than or equals to O(n*m) n=amount of left numbers, m=amount of right numbers
    public void iterateExistingExercisesNoOrder(ExerciseIteration iteration) {
        ExercisesIterator.iterateExistingExercisesNoOrder(learnedExercises, iteration);
    }

    //less than or equals to O(n*m) n=amount of left numbers, m=amount of right numbers
    public void iterateExistingIndexesNoOrder(TableCellIteration iteration) {
        ExercisesIterator.iterateExistingIndexesNoOrder(learnedExercises, iteration);
    }

    //O(1)
    public LearnedExercise getLearnedExerciseAtIndexes(int i, int j) {
        //AbstractMap.SimpleEntry's hashCode is the combination of its value's hashCodes. Can return null if nothing exists at this index.
        return learnedExercises.get(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(i, j));
    }

    //O(1)
    private LearnedExercise getLearnedExerciseAtIndexesString(String indexesString) {
        //AbstractMap.SimpleEntry's hashCode is the combination of its value's hashCodes. Can return null if nothing exists at this index.
        return learnedExercises.get(indexesString);
    }

    //O(n+m) n=amount of left numbers, m=amount of right numbers
    public LearnedExercise getLearnedExerciseByNumbers(int leftNumber, int rightNumber) {
        int leftIndex = 0;
        for (int i = 0; i < leftNumbersOrderedByTheirIndexes.size(); i++)
            if (leftNumber == leftNumbersOrderedByTheirIndexes.get(i)) {
                leftIndex = i;
                break;
            }
        int rightIndex = 0;
        for (int i = 0; i < rightNumbersOrderedByTheirIndexes.size(); i++)
            if (rightNumber == rightNumbersOrderedByTheirIndexes.get(i)) {
                rightIndex = i;
                break;
            }
        return getLearnedExerciseAtIndexes(leftIndex, rightIndex);
    }

    //O(1)
    public int getAmountOfLeftNumbers() {
        return leftNumbersOrderedByTheirIndexes.size();
    }

    //O(1)
    public int getAmountOfRightNumbers() {
        //all indexes have equal length.
        return rightNumbersOrderedByTheirIndexes.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //O(1)
    public int getTotalNumOfExercises() {
        return learnedExercises.size();
    }

    //returns the total number of times that the user has succeeded this kind of exercise. ~O(n*m) n=height, m=width
    public int getTotalNumOfSuccesses() {
        int[] numOfSuccesses = new int[1];
        iterateExistingExercisesNoOrder((indexes, exercise) -> numOfSuccesses[0]+=exercise.getNumOfLearned());
        return numOfSuccesses[0];
    }

    //returns the total number of exercises that the user has learned well. less than or equals to O(n*m) n=height, m=width
    public int getTotalNumOfLearnedWell() {
        int[] numOfLearnedWell = new int[1];
        iterateExistingExercisesNoOrder((indexes, exercise) -> {
            if (exercise.isLearned())
                numOfLearnedWell[0]++;
        });
        return numOfLearnedWell[0];
    }

    //returns a list with all of the indexes of the learned exercises from the given kind. less than or equals to O(n*m) n=height, m=width
    public LinkedList<LearnedExercise> getAllLearnedExercises() {
        final LinkedList<LearnedExercise> knownExercises = new LinkedList<>();
        iterateExistingExercisesNoOrder((indexes, exercise) -> {
            if (exercise.isLearned())
                knownExercises.add(exercise);
        });
        return knownExercises;
    }

    //returns a random known exercise. less than or equals to O(n*m) n=amount of left numbers, m=amount of right numbers.
    public LearnedExercise getRandomKnownExercise() throws NoKnownExercisesException {
        LinkedList<LearnedExercise> knownExercises = getAllLearnedExercises();
        if (knownExercises.size() != 0) {
            int randomIndex = random.nextInt(knownExercises.size());
            return knownExercises.get(randomIndex);
        }
        throw new NoKnownExercisesException();
    }

    //O(1)
    public LearnedExercise getRandomExercise() {
        int exercisePositionInHashMap = random.nextInt(existingIndexes.size());
        String indexesString = existingIndexes.get(exercisePositionInHashMap);
        return getLearnedExerciseAtIndexesString(indexesString);
    }

}
