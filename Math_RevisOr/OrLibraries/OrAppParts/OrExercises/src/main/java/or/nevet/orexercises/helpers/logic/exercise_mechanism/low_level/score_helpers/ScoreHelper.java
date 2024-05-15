package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.score_helpers;

public class ScoreHelper {

    private long score;

    public ScoreHelper() {
        score = 0;
    }

    protected void setScore(long score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    public void exerciseWasSucceeded() {
        score++;
    }

    public void wrong() {
        //I have decided that I do not want to remove scores because I believe that if I will remove scores then the multiplayer games will be more competitive and they will bring more frustration to the users.
//        if (score > 0)
//            score--;
    }
}
