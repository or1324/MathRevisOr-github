package or.nevet.orexercises.helpers.visual.multiplayer_formatting;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.io.Serializable;

import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;

public abstract class GameFormatter implements Serializable {

    private String myUnFormattedIdentifier;

    public GameFormatter(String myUnFormattedIdentifier) {
        this.myUnFormattedIdentifier = myUnFormattedIdentifier;
    }

    public abstract String formatRightUser(User user);
    public abstract String[] formatGameDataReadyUsersInBeginning(Users users);
    public abstract String formatReadyUser(User user);
    public abstract CharSequence[] formatScores(GameUsers scores);
    public abstract CharSequence[] formatScoresAtEnding(GameUsers scores);
    public abstract String formatIdentifier(String email);
    public abstract String getMyFormattedIdentifier();
    public abstract String getWinnerLeaderboardName(GameUsers users);
    public abstract String getMyLeaderboardName();
    public abstract boolean isThereATie(GameUsers users);
    public abstract boolean isMyLeaderboardScoreBetterThanTheScoreOf(String leaderboardName, GameUsers users);

    public String getMyUnFormattedIdentifier() {
        return myUnFormattedIdentifier;
    }
    public String getLeaderboardNameFromLeaderboardRowText(String leaderboardRowText) {
        return leaderboardRowText.split(" - ")[0];
    }

    protected CharSequence decorateWinnerInLeaderboard(CharSequence winner) {
        SpannableString string = new SpannableString(winner);
        string.setSpan(new ForegroundColorSpan(Color.rgb(0, 100, 0)),
                0, // start
                string.length(), // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        string.setSpan(bold, 0, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return string;
    }

}
