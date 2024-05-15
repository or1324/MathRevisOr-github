package or.nevet.orexercises.helpers.visual.multiplayer_formatting;

import java.util.Arrays;
import java.util.Collections;

import or.nevet.multiplayergame.data_objects.GameUser;
import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;

public class AllVSAllFormatter extends GameFormatter {
    public AllVSAllFormatter(String myUnFormattedIdentifier) {
        super(myUnFormattedIdentifier);
    }

    @Override
    public String formatRightUser(User user) {
        return user.getIdentifier()+" was right!";
    }

    @Override
    public String[] formatGameDataReadyUsersInBeginning(Users users) {
        String[] userIdentifiers = new String[users.getUsers().length];
        int currentIndex = 0;
        for (User user : users.getUsers())
            userIdentifiers[currentIndex++] = user.getIdentifier();
        return userIdentifiers;
    }

    @Override
    public String formatReadyUser(User user) {
        return user.getIdentifier();
    }

    @Override
    public CharSequence[] formatScores(GameUsers users) {
        return formatAndSortUsersWithScores(users.getUsers());
    }

    @Override
    public CharSequence[] formatScoresAtEnding(GameUsers users) {
        CharSequence[] usersAndTheirScores = formatScores(users);
        if (!isThereATie(users))
            usersAndTheirScores[0] = decorateWinnerInLeaderboard(usersAndTheirScores[0]);
        return usersAndTheirScores;
    }

    @Override
    public String formatIdentifier(String email) {
        return email;
    }

    @Override
    public String getMyFormattedIdentifier() {
        return getMyUnFormattedIdentifier();
    }

    @Override
    public String getWinnerLeaderboardName(GameUsers users) {
        String winnerName = null;
        long maxScore = 0;
        for (GameUser user : users.getUsers())
            if (user.getScore() > maxScore) {
                maxScore = user.getScore();
                winnerName = user.getIdentifier();
            }
        return winnerName;
    }

    @Override
    public String getMyLeaderboardName() {
        return getMyUnFormattedIdentifier();
    }

    @Override
    public boolean isThereATie(GameUsers users) {
        long maxScore = 0;
        String identifier = "";
        for (GameUser user : users.getUsers())
            if (user.getScore() >= maxScore) {
                maxScore = user.getScore();
                identifier = user.getIdentifier();
            }
        for (GameUser user : users.getUsers())
            if (user.getScore() == maxScore && !identifier.equals(user.getIdentifier()))
                return true;
        return false;
    }

    @Override
    public boolean isMyLeaderboardScoreBetterThanTheScoreOf(String leaderboardName, GameUsers users) {
        long otherUserScore = 0;
        long myScore = 0;
        for (GameUser user : users.getUsers()) {
            if (user.getIdentifier().equals(leaderboardName))
                otherUserScore = user.getScore();
            if (user.getIdentifier().equals(getMyFormattedIdentifier()))
                myScore = user.getScore();
        }
        return myScore > otherUserScore;
    }

    //O(n*log(n)) - smallest complexity possible for sorting an array of natural numbers that are divided by 10 with an unlimited size (I do not want to limit myself, even though as of now there is a limit of a score of 300). Needed in case of millions of users with very high scores.
    private CharSequence[] formatAndSortUsersWithScores(GameUser[] usersAndScores) {
        //O(n*log(n))
        Arrays.sort(usersAndScores, Collections.reverseOrder());
        //O(n)
        CharSequence[] formattedSortedUsers = new CharSequence[usersAndScores.length];
        //O(n)
        for (int i = 0; i < usersAndScores.length; i++) {
            formattedSortedUsers[i] = usersAndScores[i].getIdentifier()+" - "+usersAndScores[i].getScore();
        }
        return formattedSortedUsers;
    }
}
