package or.nevet.multiplayergame;

import or.nevet.multiplayergame.data_objects.GameExerciseResult;
import or.nevet.multiplayergame.data_objects.GameUser;
import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.ExerciseEndingNotifier;
import or.nevet.multiplayergame.game_listeners.OrReadyUsersListener;
import or.nevet.multiplayergame.game_listeners.UserRightDuringGameListener;
import or.nevet.orcloud.CloudFieldNamingHelper;
import or.nevet.orcloud.RealtimeField;
import or.nevet.orcloud.RealtimeHelper;
import or.nevet.orcloud.cloud_listeners.OrValueChangedListener;
import or.nevet.orcloud.listener_stoppers.ValueEventListenerStopper;
import or.nevet.orgeneralhelpers.constants.CloudConstants;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;

public class GameOperations {

    //creates the room and waits for someone to join. When someone joined, it shows to him that this user is ready to play and the method returns the Identifiers of the users who joined the room. When this method returns (goes async), people can start joining the room. The user should be notified of that.
    public static void createRoom(String myIdentifier, int numOfPeopleAllowedIncludingMe, char exercisesSign, OrReadyUsersListener onUserHasJoined) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(myIdentifier);
        GameOperationLowLevelStages.initializeUserGameRoomForMatching(gameRoom, exercisesSign);
        GameOperationLowLevelStages.waitForAllOfTheUsersToJoinTheRoomAsOwnerAsync(gameRoom, myIdentifier, numOfPeopleAllowedIncludingMe, onUserHasJoined);
        GameOperationLowLevelStages.signalThatIHaveJoinedTheRoom(myIdentifier, gameRoom);
    }

    //joins an existing room. Returns the exercises sign. I do not show the users as they join because maybe the owner did not open a room, and the users here are from the last game.
    public static Character joinRoom(String myIdentifier, String roomOwnerIdentifier, OrReadyUsersListener onUserHasJoined) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        roomOwnerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(roomOwnerIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(roomOwnerIdentifier);
        char exercisesSign = GeneralExerciseConstants.defaultExerciseSign;
        if (!GameOperationLowLevelStages.doesTheRoomExist(gameRoom)){
            return null;
        }
        try {
            exercisesSign = GameOperationLowLevelStages.getExercisesSign(gameRoom);
        } catch (Exception ignored) {
            //The user is for sure not connected because the exercise sign is null, so there will be a loading screen forever.
            //I do not want to tell it to the user because I know that after each game, the exercise sign will not be deleted and I can't assure that it will be deleted (at least I haven't thought about a good way to do it yet). So I will just make the user wait even if I know that the owner will not arrive, because I do not want that in some times the app will tell it to the user and in other times it will not.
        }
        GameOperationLowLevelStages.signalThatIHaveJoinedTheRoom(myIdentifier, gameRoom);
        GameOperationLowLevelStages.waitForAllUsersToJoinAsOtherUserAsync(gameRoom, myIdentifier, onUserHasJoined);
        return exercisesSign;
    }

    //the owner should call this after all of the users have joined.
    public static void initializeGame(String myIdentifier, LearnedExerciseData newExercise, Users usersWithMe) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(myIdentifier);
        GameOperationLowLevelStages.initializeUserGameRoomForGame(gameRoom, usersWithMe);
        GameOperationLowLevelStages.initializeCurrentExercise(gameRoom, newExercise);
        GameOperationLowLevelStages.signalToTheOtherUsersThatTheMatchingStageWasFinished(gameRoom, myIdentifier, usersWithMe);
    }

    public static LearnedExerciseData getReadyToPlayAsOtherUser(String myIdentifier, String ownerIdentifier, Users allUsersIdentifiersWithMe, OrReadyUsersListener onUsersAreReady) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        ownerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(ownerIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(ownerIdentifier);
        LearnedExerciseData exercise = GameOperationLowLevelStages.downloadExerciseFromRoom(gameRoom);
        waitUntilEveryoneWantsToMoveToTheNextExerciseAsync(myIdentifier, ownerIdentifier, allUsersIdentifiersWithMe, onUsersAreReady);
        return exercise;
    }

    public static void getReadyToPlayAsRoomOwner(String myIdentifier, Users allUsersIdentifiersIncludingMe, OrReadyUsersListener onUsersAreReady) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        waitUntilEveryoneWantsToMoveToTheNextExerciseAsync(myIdentifier, myIdentifier, allUsersIdentifiersIncludingMe, onUsersAreReady);
    }

    public static void waitUntilEveryoneWantsToMoveToTheNextExerciseAsync(String myIdentifier, String ownerIdentifier, Users allUsersIdentifiersWithMe, OrReadyUsersListener onUsersAreReady) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        ownerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(ownerIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(ownerIdentifier);
        GameOperationLowLevelStages.waitUntilEveryoneIsReadyAndNotifyWhenSomeoneIsReadyAndUploadMyScoreAsync(myIdentifier, gameRoom, allUsersIdentifiersWithMe, onUsersAreReady);
    }

    //If this method returns null, it means that the game is over.
    public static GameExerciseResult finishCurrentExerciseAsOwnerAndGetAllScores(String myIdentifier, LearnedExerciseData newExercise, long myScore, Users allUsersIncludingMe) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(myIdentifier);
        int exerciseNumber = (int) GameOperationLowLevelStages.getNumOfGamesDone(gameRoom);
        GameOperationLowLevelStages.initializeNextExerciseAndUploadMyScoreAndWaitForAllUsersToBeReady(myIdentifier, gameRoom, newExercise, exerciseNumber, myScore, allUsersIncludingMe.getUsers().length-1);
        boolean wasTheGameEnded = exerciseNumber >= GeneralExerciseConstants.numberOfExercisesInMultiplayerGame;
        if (wasTheGameEnded)
            GameOperationLowLevelStages.endTheGame(gameRoom);
        else {
            //if the game did not end, we need to notify the other players that they should show the scores now by changing their ready states (which are true as of now) to different value (in this case I chose to remove the collection which will remove all of the fields because I think that this is more efficient to firebase and uses less resources in the phone, and also I already have this method from initializeUsersReadyStates that I use in waitUntilEveryoneWantsToMoveToTheNextExerciseAsync). Also, I depend on that they do not exist in waitUntilEveryoneWantsToMoveToTheNextExerciseAsync.
            GameOperationLowLevelStages.initializeUsersReadyStatesDuringGame(gameRoom);
        }
        GameUser[] usersAndTheirScores = GameOperationLowLevelStages.getUsersWithScores(gameRoom);
        return new GameExerciseResult(new GameUsers(usersAndTheirScores), wasTheGameEnded);
    }

    public static GameExerciseResult finishCurrentExerciseAsOtherUserAndGetAllScores(String myIdentifier, String roomOwnerIdentifier, long myScore) {
        myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
        roomOwnerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(roomOwnerIdentifier);
        String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(roomOwnerIdentifier);
        boolean wasTheGameEnded = GameOperationLowLevelStages.uploadMyScoreAndWaitForSignalFromOwnerAboutGameContinuingSync(gameRoom, roomOwnerIdentifier, myIdentifier, myScore);
        GameUser[] usersAndTheirScores = GameOperationLowLevelStages.getUsersWithScores(gameRoom);
        return new GameExerciseResult(new GameUsers(usersAndTheirScores), wasTheGameEnded);
    }

    public static class UserWasRightInGameManager {
        //must be smaller than the number of possible exercises in a game
        private int numberOfTimesThatIWasRight;
        //number of extra digits needed to create unique string each time in order to notify even if the same user was right twice. It is equal to the length of the number of possible exercises in a game because the length of the number of times that the user was right can't exceed this. If we always add this exact number of digits and remove at the end, it will work because it will be reversible.
        private final int numOfExtraDigitsNeeded;
        public UserWasRightInGameManager() {
            numOfExtraDigitsNeeded = getNumberOfDigitsInNumber(GeneralExerciseConstants.numberOfExercisesInMultiplayerGame);
            numberOfTimesThatIWasRight = 0;
        }
        public ExerciseEndingNotifier listenToUsersRightDuringGame(String myIdentifier, String ownerIdentifier, UserRightDuringGameListener whenUserRight) {
            ownerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(ownerIdentifier);
            myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
            String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(ownerIdentifier);
            return new ExerciseEndingNotifier(listenToUsersRightDuringGameAndReverseUniqueIdentifiersBackToRegularOnes(myIdentifier, gameRoom, whenUserRight));
        }

        public void notifyThatIWasRightDuringGame(String myIdentifier, String ownerIdentifier) {
            numberOfTimesThatIWasRight++;
            ownerIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(ownerIdentifier);
            myIdentifier = CloudFieldNamingHelper.replaceDotsWithSpaces(myIdentifier);
            myIdentifier = getAReversibleIdentifierThatWasNeverUsedBeforeFromMyIdentifier(myIdentifier);
            String gameRoom = GameOperationLowLevelStages.getGameRoomForOwnerIdentifier(ownerIdentifier);
            RealtimeHelper.RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(new RealtimeField(gameRoom+CloudConstants.cloudLocationSeparator+CloudConstants.lastUserRightGameProperty, myIdentifier), null);
        }
        
        private ValueEventListenerStopper listenToUsersRightDuringGameAndReverseUniqueIdentifiersBackToRegularOnes(String myIdentifier, String gameRoom, UserRightDuringGameListener whenUserRight) {
            return RealtimeHelper.RealtimeHelperAsync.listenToValueChangesAsync(gameRoom + CloudConstants.cloudLocationSeparator + CloudConstants.lastUserRightGameProperty, new OrValueChangedListener() {
                @Override
                public void onChanged(RealtimeField field) {
                    String userIdentifier = reverseUniqueIdentifier(field.getFieldValue().toString());
                    if (!userIdentifier.equals(myIdentifier))
                        whenUserRight.userRight(new User(CloudFieldNamingHelper.restoreDotsAfterReplacementWithSpaces(userIdentifier)));
                }
            });
        }
        
        private String getAReversibleIdentifierThatWasNeverUsedBeforeFromMyIdentifier(String myIdentifier) {
            String uniqueIdentifier = myIdentifier;
            int numberOfZerosToAddFromLeft = numOfExtraDigitsNeeded -getNumberOfDigitsInNumber(numberOfTimesThatIWasRight);
            for (int i = 0; i < numberOfZerosToAddFromLeft; i++)
                uniqueIdentifier = uniqueIdentifier.concat("0");
            uniqueIdentifier = uniqueIdentifier.concat(String.valueOf(numberOfTimesThatIWasRight));
            return uniqueIdentifier;
        }
        
        private String reverseUniqueIdentifier(String uniqueIdentifier) {
            int numberOfDigitsToRemove = numOfExtraDigitsNeeded;
            return uniqueIdentifier.substring(0, uniqueIdentifier.length()-numberOfDigitsToRemove);
        }
        private int getNumberOfDigitsInNumber(int number) {
            return String.valueOf(number).length();
        }
    }
}
