package or.nevet.mathrevisorusermanager;


import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.HashMap;

import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;

class User implements Serializable {

    private String email;
    private String userName;
    private long score;
    private HashMap<String, LearnedExercisesData> learnedExercises;

    public User(String email, String userName, long score, HashMap<String, LearnedExercisesData> learnedExercises) {
        this.email = email;
        this.userName = userName;
        this.score = score;
        this.learnedExercises = learnedExercises;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, LearnedExercisesData> getLearnedExercises() {
        return learnedExercises;
    }

    public long getScore() {
        return score;
    }

    public String getUserName() {
        return userName;
    }

    public void setLearnedExercises(HashMap<String, LearnedExercisesData> learnedExercises) {
        this.learnedExercises = learnedExercises;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
