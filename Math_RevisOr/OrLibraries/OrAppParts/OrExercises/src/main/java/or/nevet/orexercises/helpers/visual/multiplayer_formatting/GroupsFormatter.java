package or.nevet.orexercises.helpers.visual.multiplayer_formatting;

import android.content.Context;

import java.util.LinkedList;

import or.nevet.multiplayergame.data_objects.GameUser;
import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

public class GroupsFormatter extends GameFormatter {

    private final int groupNum;

    public GroupsFormatter(int groupNum, String myUnFormattedIdentifier) {
        super(myUnFormattedIdentifier);
        this.groupNum = groupNum;
    }

    @Override
    public String formatRightUser(User user) {
        String[] userParts = user.getIdentifier().split("_");
        String userEmail = userParts[0];
        String groupNumber = userParts[1];
        return userEmail+" from group "+groupNumber+" was right!";
    }

    //O(n)
    @Override
    public String[] formatGameDataReadyUsersInBeginning(Users users) {
        LinkedList<String> group1Users = new LinkedList<>();
        LinkedList<String> group2Users = new LinkedList<>();
        for (User user : users.getUsers()) {
            String[] userParts = user.getIdentifier().split("_");
            String userEmail = userParts[0];
            String groupNumber = userParts[1];
            if (groupNumber.equals("1")) {
                group1Users.add(userEmail);
            } else /* groupNumber is equal to "2" */{
                group2Users.add(userEmail);
            }
        }
        String[] formattedUsers = new String[users.getUsers().length+2];
        formattedUsers[0] = "group 1:";
        int currentIndex = 1;
        for (String user : group1Users)
            formattedUsers[currentIndex++] = user;
        formattedUsers[currentIndex++] = "group 2:";
        for (String user : group2Users)
            formattedUsers[currentIndex++] = user;
        return formattedUsers;
    }

    @Override
    public String formatReadyUser(User user) {
        String[] userParts = user.getIdentifier().split("_");
        String userEmail = userParts[0];
        String groupNumber = userParts[1];
        return userEmail+" from group "+groupNumber;
    }

    @Override
    public String[] formatScores(GameUsers users) {
        LinkedList<CharSequence> formattedScoresList = new LinkedList<>();
        long group1Score = getGroupScore(1, users.getUsers());
        long group2Score = getGroupScore(2, users.getUsers());
        formattedScoresList.add("group 1 - "+group1Score);
        addGroupInsights(1, users.getUsers(), formattedScoresList);
        formattedScoresList.add("group 2 - "+group2Score);
        addGroupInsights(2, users.getUsers(), formattedScoresList);
        String[] formattedScoresArray = new String[formattedScoresList.size()];
        formattedScoresList.toArray(formattedScoresArray);
        return formattedScoresArray;
    }

    @Override
    public CharSequence[] formatScoresAtEnding(GameUsers users) {
        LinkedList<CharSequence> formattedScoresList = new LinkedList<>();
        long group1Score = getGroupScore(1, users.getUsers());
        long group2Score = getGroupScore(2, users.getUsers());
        CharSequence group1ScoreRowTextInLeaderboard = "group 1 - "+group1Score;
        CharSequence group2ScoreRowTextInLeaderboard = "group 2 - "+group2Score;
        if (!isThereATie(users)) {
            if (Math.max(group1Score, group2Score) == group1Score)
                group1ScoreRowTextInLeaderboard = decorateWinnerInLeaderboard(group1ScoreRowTextInLeaderboard);
            else
                group2ScoreRowTextInLeaderboard = decorateWinnerInLeaderboard(group2ScoreRowTextInLeaderboard);
        }
        formattedScoresList.add(group1ScoreRowTextInLeaderboard);
        addGroupInsights(1, users.getUsers(), formattedScoresList);
        formattedScoresList.add(group2ScoreRowTextInLeaderboard);
        addGroupInsights(2, users.getUsers(), formattedScoresList);
        CharSequence[] formattedScoresArray = new CharSequence[formattedScoresList.size()];
        formattedScoresList.toArray(formattedScoresArray);
        return formattedScoresArray;
    }

    @Override
    public String formatIdentifier(String identifier) {
        return identifier.concat("_").concat(String.valueOf(groupNum));
    }

    @Override
    public String getMyFormattedIdentifier() {
        return formatIdentifier(getMyUnFormattedIdentifier());
    }

    @Override
    public String getWinnerLeaderboardName(GameUsers users) {
        long group1Score = getGroupScore(1, users.getUsers());
        long group2Score = getGroupScore(2, users.getUsers());
        if (Math.max(group1Score, group2Score) == group1Score)
            return "group 1";
        else
            return "group 2";
    }

    @Override
    public String getMyLeaderboardName() {
        return "group "+groupNum;
    }

    @Override
    public boolean isThereATie(GameUsers users) {
        long group1Score = getGroupScore(1, users.getUsers());
        long group2Score = getGroupScore(2, users.getUsers());
        return group1Score == group2Score;
    }

    @Override
    public boolean isMyLeaderboardScoreBetterThanTheScoreOf(String leaderboardName, GameUsers users) {
        long otherUserGroupScore = getGroupScore(Integer.parseInt(leaderboardName.split(" ")[1]), users.getUsers());
        long myGroupScore = getGroupScore(groupNum, users.getUsers());
        return myGroupScore > otherUserGroupScore;
    }

    private void addGroupInsights(int groupNum, GameUser[] users, LinkedList<CharSequence> listToAddTo) {
        long myScore = getMyScoreFromGameUserArray(users);
        for (GameUser user : users) {
            String otherUserEmail = user.getIdentifier().split("_")[0];
            String otherUserGroup = user.getIdentifier().split("_")[1];
            if (!otherUserEmail.equals(getMyUnFormattedIdentifier()) && otherUserGroup.equals(String.valueOf(groupNum))) {
                String insight = getScoresInsight(myScore, user.getScore(), otherUserEmail);
                if (insight != null)
                    listToAddTo.add("Insight: "+insight);
            }
        }
    }

    private String getScoresInsight(long currentUserScore, long otherUserScore, String otherUserEmail) {
        //The first condition that is right will be chosen (if the scores are both equal and divisible, it will say that they are equal)
        if (currentUserScore*otherUserScore == 42)
            return "Your score multiplied by the score of "+otherUserEmail+" is equal to 42 - the answer to the ultimate question of life, the universe, and everything";
        else if (currentUserScore == otherUserScore)
            return "Your score is equal to the score of "+otherUserEmail;
        else if (currentUserScore == otherUserScore*2)
            return "Your score is double the score of "+otherUserEmail;
        else if (otherUserScore > 0 && currentUserScore > 0 && currentUserScore%otherUserScore == 0)
            return "Your score is divisible by the score of "+otherUserEmail;
        else if (currentUserScore > 0 && otherUserScore > 0 && otherUserScore%currentUserScore == 0)
            return "The score of "+otherUserEmail+" is divisible by your score";
        return null;
    }

    private long getMyScoreFromGameUserArray(GameUser[] users) {
        for (GameUser user : users)
            if (getUserEmail(user).equals(getMyUnFormattedIdentifier()))
                return user.getScore();
        throw new RuntimeException(UserMessagesConstants.currentUserEmailWasNotInArray);
    }

    private long getGroupScore(int groupNum, GameUser[] users) {
        long groupScore = 0;
        for (GameUser user : users)
            if (getUserGroupNum(user) == groupNum)
                groupScore+=user.getScore();
        return groupScore;
    }

    private int getUserGroupNum(User user) {
        return Integer.parseInt(user.getIdentifier().split("_")[1]);
    }

    private String getUserEmail(User user) {
        return user.getIdentifier().split("_")[0];
    }

}
