package or.nevet.mathrevisorusermanager;

import java.util.HashMap;

import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExercisesData;

public class UserObject {
    private final User user;
    UserObject(User user) {
        this.user = user;
    }
    public long getScore() {
        return user.getScore();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public HashMap<String, LearnedExercisesData> getLearnedExercises() {
        return user.getLearnedExercises();
    }
}
