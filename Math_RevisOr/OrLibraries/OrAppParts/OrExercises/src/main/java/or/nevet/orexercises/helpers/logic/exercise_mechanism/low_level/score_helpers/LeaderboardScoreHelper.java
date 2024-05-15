package or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.score_helpers;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;

import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;

//Everytime someone calls a method from here, he should make sure that he calls from a background thread, because this class deals with the background logic. Anyway, it is possible to access this class from every thread.
public class LeaderboardScoreHelper extends ScoreHelper {

    AppCompatTextView scoreView;
    Context context;

    public LeaderboardScoreHelper(AppCompatTextView scoreView, Context context) {
        super();
        setScore(UserManager.getCurrentUserObject(context).getScore());
        this.scoreView = scoreView;
        this.context = context;
        writeScoreOnScreen();
    }

    public void exerciseWasSucceeded() {
        super.exerciseWasSucceeded();
        UserManager.updateAndSaveScore(getScore(), context);
        writeScoreOnScreen();
    }

    public void wrong() {
        //I have decided that I do not want to remove scores because I believe that if I will remove scores then the multiplayer games will be more competitive and they will bring more frustration to the users. Also, the multiplayer will look worse. Also, it does not really matter if the score will go down when someone is wrong in the regular game because it will not tell how good the user is anyway. It will be a combination of how much the user plays with how good he is. If I only give scores it will be the same thing, but with more respect to how much the user plays.
//        if (getScore() > 0) {
//            super.wrong();
//            UserManager.updateAndSaveScore(getScore(), context);
//            writeScoreOnScreen();
//        }
    }
    private void writeScoreOnScreen() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> scoreView.setText(String.valueOf(getScore())));
    }
}
